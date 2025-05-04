package grad.project.padelytics.features.results.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
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
        _isFetching.value = true

        db.collection("matches").get()
            .addOnSuccessListener { documents ->
                val allMatches = mutableListOf<MatchData>()
                var processedCount = 0

                if (documents.isEmpty) {
                    _matchData.value = emptyList()
                    _isFetching.value = false
                    return@addOnSuccessListener
                }

                for (doc in documents) {
                    val playerIds = listOf(
                        doc.getString("player1"),
                        doc.getString("player2"),
                        doc.getString("player3"),
                        doc.getString("player4")
                    )

                    if (playerIds.any { it == null }) {
                        processedCount++
                        if (processedCount == documents.size()) {
                            _matchData.value = allMatches
                            _isFetching.value = false
                        }
                        continue
                    }

                    val playersInfo = mutableListOf<PlayerInfo>()
                    var fetchedPlayers = 0

                    playerIds.forEach { uid ->
                        db.collection("users").document(uid!!).get()
                            .addOnSuccessListener { userDoc ->
                                val firstName = userDoc.getString("firstName") ?: "Unknown"
                                val photo = userDoc.getString("photo") ?: ""
                                val level = userDoc.getString("level") ?: "N/A"

                                playersInfo.add(PlayerInfo(uid, firstName, photo, level))
                                fetchedPlayers++

                                if (fetchedPlayers == 4) {
                                    allMatches.add(
                                        MatchData(
                                            players = playersInfo,
                                            court = doc.getString("court") ?: "Unknown",
                                            formattedTime = doc.getString("formattedTime") ?: "",
                                            timestamp = doc.getLong("timestamp") ?: 0
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