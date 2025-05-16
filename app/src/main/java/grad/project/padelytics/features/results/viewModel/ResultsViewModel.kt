package grad.project.padelytics.features.results.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import grad.project.padelytics.features.results.data.MatchData
import grad.project.padelytics.features.results.data.PlayerInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ResultsViewModel: ViewModel() {
    private val _matchData = MutableStateFlow<List<MatchData>>(emptyList())
    val matchData: StateFlow<List<MatchData>> = _matchData

    private val _isFetching = MutableStateFlow(false)
    val isFetching: StateFlow<Boolean> = _isFetching.asStateFlow()

    init {
        fetchMatchesData()
    }

    private fun fetchMatchesData() {
        val db = FirebaseFirestore.getInstance()
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        _isFetching.value = true

        db.collection("users")
            .document(currentUserId)
            .collection("uploadedMatches")
            .get()
            .addOnSuccessListener { documents ->
                val allMatches = mutableListOf<MatchData>()
                var processedCount = 0

                if (documents.isEmpty) {
                    _matchData.value = emptyList()
                    _isFetching.value = false
                    return@addOnSuccessListener
                }

                for (doc in documents) {
                    val playerIds = doc.get("players") as? List<*>

                    if (playerIds == null || playerIds.size != 4) {
                        processedCount++
                        if (processedCount == documents.size()) {
                            _matchData.value = allMatches
                            _isFetching.value = false
                        }
                        continue
                    }

                    val playersInfo = MutableList<PlayerInfo?>(4) { null }
                    var fetchedPlayers = 0

                    playerIds.forEachIndexed { index, uid ->
                        db.collection("users").document(uid.toString()).get()
                            .addOnSuccessListener { userDoc ->
                                val firstName = userDoc.getString("firstName") ?: "Unknown"
                                val photo = userDoc.getString("photo") ?: ""
                                val level = userDoc.getString("level") ?: "N/A"
                                playersInfo[index] = PlayerInfo(uid.toString(), firstName, photo, level)
                                fetchedPlayers++

                                if (fetchedPlayers == 4) {
                                    val finalPlayers = playersInfo.map { it!! }
                                    allMatches.add(
                                        MatchData(
                                            matchId = doc.id,
                                            players = finalPlayers,
                                            court = doc.getString("court") ?: "Unknown",
                                            formattedTime = doc.getString("formattedTime") ?: "",
                                            timestamp = doc.getLong("timestamp") ?: 0,
                                            matchUrl = doc.getString("matchUrl") ?: ""
                                        )
                                    )
                                    processedCount++
                                    if (processedCount == documents.size()) {
                                        _matchData.value = allMatches
                                        _isFetching.value = false
                                    }
                                }
                            }
                            .addOnFailureListener {
                                fetchedPlayers++
                                processedCount++
                                if (processedCount == documents.size()) {
                                    _matchData.value = allMatches
                                    _isFetching.value = false
                                }
                            }
                    }
                }
            }
            .addOnFailureListener {
                Log.e("MatchFetch", "Failed to fetch matches", it)
                _matchData.value = emptyList()
                _isFetching.value = false
            }
    }
}