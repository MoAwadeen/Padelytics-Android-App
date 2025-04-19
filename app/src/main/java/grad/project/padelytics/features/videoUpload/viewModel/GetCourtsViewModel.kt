package grad.project.padelytics.features.videoUpload.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class FavouriteCourtsViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _courtsNames = MutableStateFlow<List<String>>(emptyList())
    val courtsNames: StateFlow<List<String>> = _courtsNames

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val TAG = "FavouriteCourtsVM"

    fun fetchCourtsNames() {
        val userId = auth.currentUser?.uid ?: return

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val snapshot = db.collection("users")
                    .document(userId)
                    .collection("favouriteCourts")
                    .get()
                    .await()

                Log.d(TAG, "Fetched ${snapshot.size()} favorite courts")

                val names = snapshot.documents.mapNotNull {
                    val courtName = it.getString("courtName")
                    if (courtName == null) {
                        Log.w(TAG, "Missing courtName in document ${it.id}")
                    }
                    courtName
                }

                _courtsNames.value = names
            } catch (e: Exception) {
                Log.e(TAG, "Failed to fetch court names", e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}
