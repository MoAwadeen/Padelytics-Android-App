package grad.project.padelytics.features.tournaments.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import grad.project.padelytics.features.tournaments.data.Tournament
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class TournamentsViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val _tournaments = MutableStateFlow<List<Tournament>>(emptyList())
    val tournaments: StateFlow<List<Tournament>> = _tournaments
    private val db = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    private val _isFetching = MutableStateFlow(false)
    val isFetching: StateFlow<Boolean> = _isFetching.asStateFlow()

    init {
        fetchTournaments()
    }

    fun fetchTournaments() {
        viewModelScope.launch {
            _isFetching.value = true
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
                    _isFetching.value = false
                }
                .addOnFailureListener { e ->
                    e.printStackTrace()
                    _isFetching.value = false
                }
        }
    }

    fun getTournamentById(tournamentId: String?): Flow<Tournament?> = flow {
        if (tournamentId != null) {
            _isFetching.value = true
            try {
                Log.d("TournamentsViewModel", "Fetching document: $tournamentId")
                val snapshot = db.collection("tournaments")
                    .document(tournamentId)
                    .get()
                    .await()

                if (snapshot.exists()) {
                    val tournament = snapshot.toObject(Tournament::class.java)
                    emit(tournament)
                    _isFetching.value = false
                } else {
                    Log.d("TournamentsViewModel", "Tournament not found")
                    emit(null)
                    _isFetching.value = false
                }
            } catch (e: Exception) {
                Log.e("TournamentsViewModel", "Error loading tournament", e)
                emit(null)
            }
        }
    }.flowOn(Dispatchers.IO)

    fun saveFavoriteTournament(tournament: Tournament, onComplete: (Boolean) -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val db = FirebaseFirestore.getInstance()
            val favoriteTournamentRef = db.collection("users")
                .document(userId)
                .collection("favoriteTournaments")
                .document(tournament.id)

            val favoriteData = hashMapOf(
                "id" to tournament.id,
                "tournamentName" to tournament.tournamentName,
                "image" to tournament.image,
                "prize" to tournament.prize,
                "location" to tournament.location,
                "url" to tournament.url
            )

            favoriteTournamentRef.set(favoriteData)
                .addOnSuccessListener {
                    onComplete(true)
                }
                .addOnFailureListener {
                    onComplete(false)
                }
        } else {
            onComplete(false)
        }
    }

    fun removeFavoriteTournament(tournamentId: String, onComplete: (Boolean) -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val db = FirebaseFirestore.getInstance()
            db.collection("users")
                .document(userId)
                .collection("favoriteTournaments")
                .document(tournamentId)
                .delete()
                .addOnSuccessListener {
                    onComplete(true)
                }
                .addOnFailureListener {
                    onComplete(false)
                }
        } else {
            onComplete(false)
        }
    }

    fun checkIfFavorite(tournamentId: String, onResult: (Boolean) -> Unit) {
        if (userId != null) {
            db.collection("users")
                .document(userId)
                .collection("favoriteTournaments")
                .document(tournamentId)
                .get()
                .addOnSuccessListener { document ->
                    onResult(document.exists())
                }
                .addOnFailureListener {
                    onResult(false)
                }
        } else {
            onResult(false)
        }
    }
}