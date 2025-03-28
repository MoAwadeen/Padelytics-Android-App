package grad.project.padelytics.features.tournaments.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import grad.project.padelytics.features.tournaments.data.Tournament
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TournamentsViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val _tournaments = MutableStateFlow<List<Tournament>>(emptyList())
    val tournaments: StateFlow<List<Tournament>> = _tournaments

    fun fetchTournaments() {
        viewModelScope.launch {
            firestore.collection("tournaments")
                .get()
                .addOnSuccessListener { result ->
                    val tournamentList = result.map { document ->
                        Tournament(
                            tournamentName = document.getString("tournamentName") ?: "",
                            image = document.getString("image") ?: "",
                            location = document.getString("location") ?: "",
                            prize = document.getString("prize") ?: "",
                            registrationFees = document.getString("registrationfees") ?: "",
                            date = document.getString("date") ?: "",
                            type = document.getString("type") ?: "",
                            url = document.getString("url") ?: "",
                            id = document.getString("id") ?: ""
                        )
                    }
                    _tournaments.value = tournamentList
                }
                .addOnFailureListener { e ->
                    e.printStackTrace()
                }
        }
    }
}
