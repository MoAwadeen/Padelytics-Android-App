package grad.project.padelytics.features.profile.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import grad.project.padelytics.data.UserProfileModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _userProfile = MutableStateFlow<UserProfileModel?>(null)  // ✅ Use MutableStateFlow
    val userProfile: StateFlow<UserProfileModel?> get() = _userProfile  // ✅ Expose as StateFlow

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
}
