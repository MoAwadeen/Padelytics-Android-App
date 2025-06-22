package grad.project.padelytics.features.auth.viewModel

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import grad.project.padelytics.R
import grad.project.padelytics.data.UserModel
import grad.project.padelytics.navigation.Routes
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val auth: FirebaseAuth = Firebase.auth
    private val firestore = Firebase.firestore

    fun signup(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        userName: String,
        photo: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        viewModelScope.launch {
            try {

                val authResult = auth.createUserWithEmailAndPassword(email, password).await()
                val userId = authResult.user?.uid ?: throw Exception("User creation failed")

                val userModel = UserModel(
                    firstName = firstName,
                    lastName = lastName,
                    userName = userName,
                    uid = userId,
                    email = email,
                    password = password,
                    photo = photo
                )

                firestore.collection("users")
                    .document(userId)
                    .set(userModel)
                    .await()

                onResult(true, null)
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Signup failed", e)
                onResult(false, e.message ?: "Signup failed")
            }
        }
    }

    fun login(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, password).await()
                onResult(true, null)
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Login failed", e)
                onResult(false, e.message ?: "Login failed")
            }
        }
    }

    fun googleSignIn(launcher: ActivityResultLauncher<Intent>, onResult: (Boolean, String?) -> Unit) {
        try {
            val signInClient = GoogleSignIn.getClient(
                getApplication(),
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getApplication<Application>().getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()
            )
            signInClient.signOut().addOnCompleteListener {
                launcher.launch(signInClient.signInIntent) // Ensure the account selection prompt appears
            }
        } catch (e: Exception) {
            onResult(false, "Failed to initiate Google Sign-In")
            Log.e("AuthViewModel", "Google Sign-In error", e)
        }
    }



    fun handleGoogleSignInToken(idToken: String, onResult: (Boolean, Boolean, String?) -> Unit) {
        viewModelScope.launch {
            try {
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                val authResult = auth.signInWithCredential(credential).await()

                authResult.user?.let { user ->
                    val userDoc = firestore.collection("users").document(user.uid).get().await()

                    if (userDoc.exists()) {
                        // User already exists, navigate to Home
                        onResult(true, true, null)
                    } else {
                        // New user, navigate to second signup page
                        val names = user.displayName?.split(" ") ?: listOf("User", "")
                        val userModel = UserModel(
                            firstName = names.first(),
                            lastName = names.getOrElse(1) { "" },
                            uid = user.uid,
                            email = user.email ?: "",
                            password = "",
                            photo = user.photoUrl?.toString() ?: "",
                            userName = (user.displayName?.split(" ") ?: listOf("User", "")).toString()
                        )
                        firestore.collection("users").document(user.uid).set(userModel).await()
                        onResult(true, false, null)
                    }
                } ?: onResult(false, false, "User not found")
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Google Sign-In failed", e)
                onResult(false, false, "Authentication failed. Please try again.")
            }
        }
    }


    fun addExtraFeature(gender: String, level: String,city: String,date: String ,onResult: (Boolean, String?) -> Unit) {
        val userId = auth.currentUser?.uid ?: run {
            onResult(false, "User not found")
            return
        }

        viewModelScope.launch {
            try {
                val userUpdates = mapOf(
                    "gender" to gender,
                    "level" to level,
                    "city" to city,
                    "date" to date
                )

                firestore.collection("users")
                    .document(userId)
                    .update(userUpdates)
                    .await()

                onResult(true, null)
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Failed to update user data", e)
                onResult(false, e.message ?: "Failed to update profile")
            }
        }
    }

    fun saveUserToFirestore(user: FirebaseUser) {
        val db = FirebaseFirestore.getInstance()
        val userRef = db.collection("users").document(user.uid)

        val userData = hashMapOf(
            "uid" to user.uid,
            "email" to user.email,
            "name" to (user.displayName ?: "Guest")
        )

        userRef.set(userData, SetOptions.merge())
            .addOnSuccessListener { Log.d("Firestore", "User saved successfully") }
            .addOnFailureListener { Log.e("Firestore", "Error saving user", it) }
    }


    fun logout(navController: NavController) {
        val signInClient = GoogleSignIn.getClient(
            getApplication(),
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        )

        signInClient.signOut().addOnCompleteListener {
            FirebaseAuth.getInstance().signOut()
            navController.navigate(Routes.AUTH) {
                popUpTo(Routes.HOME) { inclusive = true }
            }
        }.addOnFailureListener { e ->
            Log.e("AuthViewModel", "Google sign-out failed", e)
            FirebaseAuth.getInstance().signOut()
            navController.navigate(Routes.AUTH) {
                popUpTo(Routes.HOME) { inclusive = true }
            }
        }
    }

    fun updateUsername(newUsername: String, onResult: (Boolean, String?) -> Unit) {
        val userId = auth.currentUser?.uid ?: run {
            onResult(false, "User not logged in")
            return
        }

        viewModelScope.launch {
            try {

                val existingUsers = firestore.collection("users")
                    .whereEqualTo("userName", newUsername)
                    .get()
                    .await()

                val usernameTaken = existingUsers.any { it.id != userId }

                if (usernameTaken) {
                    onResult(false, "Username is already taken")
                    return@launch
                }

                firestore.collection("users")
                    .document(userId)
                    .update("userName", newUsername)
                    .await()
                onResult(true, null)

            } catch (e: Exception) {
                Log.e("AuthViewModel", "Failed to update username", e)
                onResult(false, e.message ?: "Failed to update username")
            }
        }
    }

    fun checkUsernameAvailable(
        username: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val existingUsers = firestore.collection("users")
                    .whereEqualTo("userName", username) // Make sure this matches your Firestore field
                    .get()
                    .await()

                if (existingUsers.isEmpty) {
                    onResult(true, null)
                } else {
                    onResult(false, "Username is already taken")
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "checkUsernameAvailable failed", e)
                onResult(false, "Failed to check username availability")
            }
        }
    }


}