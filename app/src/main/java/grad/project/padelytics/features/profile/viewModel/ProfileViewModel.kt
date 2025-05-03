package grad.project.padelytics.features.profile.viewModel

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import grad.project.padelytics.data.UserProfileModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _userProfile = MutableStateFlow<UserProfileModel?>(null)
    val userProfile: StateFlow<UserProfileModel?> get() = _userProfile


    private val _imageUrl = MutableStateFlow<String?>(null)
    val imageUrl = _imageUrl.asStateFlow()

    fun fetchUserProfile() {
        val userId = auth.currentUser?.uid ?: return

        viewModelScope.launch {
            try {
                val document = firestore.collection("users").document(userId).get().await()
                if (document.exists()) {
                    _userProfile.value = document.toObject<UserProfileModel>()
                } else {
                    Log.e("ProfileViewModel", "User profile not found")
                }
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error fetching user profile", e)
            }
        }
    }

    fun uploadImage(uri: Uri, context: Context) {

        val cloudinary = Cloudinary("cloudinary://711966464192934:_6CYk7HyN4lF9ZG2NDjWrwAvDAw@dqcgb73mf")


        viewModelScope.launch(Dispatchers.IO) {
            val inputStream = context.contentResolver.openInputStream(uri)
            inputStream?.use { stream ->
                try {
                    val result = cloudinary.uploader().upload(stream, ObjectUtils.asMap(
                        "upload_preset", "Padelytics"
                    ))
                    val imageUrl = result["secure_url"] as String
                    _imageUrl.value = imageUrl
                    addExtraFeature(imageUrl){ success, errorMsg ->
                        if (success) {
                           Toast.makeText(context, "Image uploaded successfully", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, errorMsg ?: "Something went wrong", Toast.LENGTH_SHORT).show()
                        }}
                } catch (e: Exception) {
                    Log.e("CloudinaryUpload", "Failed to upload image", e)
                }
            }
        }
    }

    private fun addExtraFeature(photo: String, onResult: (Boolean, String?) -> Unit) {
        val userId = auth.currentUser?.uid ?: run {
            onResult(false, "User not found")
            return
        }

        viewModelScope.launch {
            try {
                val userUpdates = mapOf(
                    "photo" to photo,
                )

                firestore.collection("users")
                    .document(userId)
                    .update(userUpdates)
                    .await()

                onResult(true, null)
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Failed to update user data", e)
                onResult(false, e.message ?: "Failed to update profile")
            }
        }
    }
}