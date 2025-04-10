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

class FavouriteTournamentsViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val _tournamentNames = MutableStateFlow<List<String>>(emptyList())
    val tournamentNames: StateFlow<List<String>> = _tournamentNames

    private val TAG = "FavouriteTournamentsVM"


    fun fetchTournamentNames() {

        val userId = auth.currentUser?.uid ?: return

        viewModelScope.launch {
            try {
                val snapshot = db.collection("users")
                    .document(userId)
                    .collection("favoriteTournaments")
                    .get()
                    .await()

                val names = snapshot.documents.mapNotNull {
                    val name = it.getString("tournamentId")
                    if (name == null) {
                        Log.w(TAG, "Missing tournamentName in document ${it.id}")
                    }
                    name
                }

                _tournamentNames.value = names
            } catch (e: Exception) {
                Log.e(TAG, "Failed to fetch tournament names", e)
            }
        }
    }
}
