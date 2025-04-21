package grad.project.padelytics.features.home.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private var firstName: String? = null

    fun fetchFirstName(onResult: (String?) -> Unit) {
        val userId = auth.currentUser?.uid ?: return onResult( "User not logged in")

        viewModelScope.launch {
            firestore.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        firstName = document.getString("firstName") ?: "User"
                        onResult(firstName)
                    } else {
                        onResult("User data not found")
                    }
                }
                .addOnFailureListener { exception ->
                    onResult(exception.localizedMessage)
                }
        }
    }
}