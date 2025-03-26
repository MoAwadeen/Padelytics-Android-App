package grad.project.padelytics.features.auth.viewModel

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import grad.project.padelytics.R
import grad.project.padelytics.data.UserModel
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
        onResult: (Boolean, String?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val authResult = auth.createUserWithEmailAndPassword(email, password).await()
                val userId = authResult.user?.uid ?: throw Exception("User creation failed")

                val userModel = UserModel(
                    firstName = firstName,
                    lastName = lastName,
                    uid = userId,
                    email = email,
                    password = password
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
            launcher.launch(signInClient.signInIntent)
        } catch (e: Exception) {
            onResult(false, "Failed to initiate Google Sign-In")
            Log.e("AuthViewModel", "Google Sign-In error", e)
        }
    }

    fun handleGoogleSignInToken(idToken: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            try {
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                val authResult = auth.signInWithCredential(credential).await()

                authResult.user?.let { user ->
                    // Check if user exists in Firestore
                    val userDoc = firestore.collection("users").document(user.uid).get().await()

                    if (!userDoc.exists()) {
                        // Create new user if doesn't exist
                        val names = user.displayName?.split(" ") ?: listOf("User", "")
                        val userModel = UserModel(
                            firstName = names.first(),
                            lastName = names.getOrElse(1) { "" },
                            uid = user.uid,
                            email = user.email ?: "",
                            password = ""
                        )
                        firestore.collection("users").document(user.uid).set(userModel).await()
                    }
                    onResult(true, null)
                } ?: onResult(false, "User not found")
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Google Sign-In failed", e)
                onResult(false, "Authentication failed. Please try again.")
            }
        }
    }

    fun addExtraFeature(gender: String, level: String, onResult: (Boolean, String?) -> Unit) {
        val userId = auth.currentUser?.uid ?: run {
            onResult(false, "User not found")
            return
        }

        viewModelScope.launch {
            try {
                val userUpdates = mapOf(
                    "gender" to gender,
                    "level" to level
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

    fun logout(onResult: () -> Unit) {
        auth.signOut()
        onResult()
    }
}