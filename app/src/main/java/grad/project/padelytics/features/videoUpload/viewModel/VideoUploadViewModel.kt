package grad.project.padelytics.features.videoUpload.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import grad.project.padelytics.features.videoUpload.data.FriendData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class VideoUploadViewModel: ViewModel() {
    private val _selectedVideoUri = MutableStateFlow<Uri?>(null)
    //val selectedVideoUri: StateFlow<Uri?> = _selectedVideoUri

    private val _searchResult = MutableStateFlow<Pair<String, String>?>(null)
    val searchResult: StateFlow<Pair<String, String>?> = _searchResult

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _friendsList = MutableStateFlow<List<FriendData>>(emptyList())
    val friendsList: StateFlow<List<FriendData>> = _friendsList

    private val _isLoadingFriends = MutableStateFlow(false)
    val isLoadingFriends: StateFlow<Boolean> = _isLoadingFriends

    private val firestore = FirebaseFirestore.getInstance()

    private val _selectedFriends = MutableStateFlow<List<FriendData?>>(listOf(null, null, null, null))
    val selectedFriends: StateFlow<List<FriendData?>> = _selectedFriends

    fun setSelectedVideo(uri: Uri) {
        _selectedVideoUri.value = uri
    }

    fun searchUsername(username: String) {
        _isLoading.value = true
        firestore.collection("users")
            .whereEqualTo("userName", username)
            .limit(1)
            .get()
            .addOnSuccessListener { documents ->
                _isLoading.value = false
                if (!documents.isEmpty) {
                    val doc = documents.documents[0]
                    val foundUsername = doc.getString("userName") ?: return@addOnSuccessListener
                    _searchResult.value = Pair(foundUsername, doc.id)
                } else {
                    _searchResult.value = null
                }
            }
            .addOnFailureListener {
                _isLoading.value = false
                _searchResult.value = null
            }
    }

    fun clearResult() {
        _searchResult.value = null
    }

    fun addUserToFriends(currentUserId: String, friendDocId: String) {
        val usersRef = firestore.collection("users")

        usersRef.document(friendDocId).get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    val username = doc.getString("userName") ?: return@addOnSuccessListener
                    val firstName = doc.getString("firstName") ?: ""
                    val lastName = doc.getString("lastName") ?: ""
                    val photo = doc.getString("photo") ?: ""

                    val friendData = mapOf(
                        "uid" to friendDocId,
                        "userName" to username,
                        "firstName" to firstName,
                        "lastName" to lastName,
                        "photo" to photo
                    )

                    usersRef.document(currentUserId)
                        .collection("playerFriends")
                        .document(friendDocId)
                        .set(friendData)
                        .addOnSuccessListener {
                            Log.d("FriendAdd", "Friend added successfully")
                            fetchFriendsList() // Refresh friends list
                        }
                        .addOnFailureListener { e ->
                            Log.e("FriendAdd", "Failed to add friend", e)
                        }
                }
            }
            .addOnFailureListener {
                Log.e("FriendFetch", "Could not fetch friend document", it)
            }
    }

    fun fetchFriendsList() {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        _isLoadingFriends.value = true

        val allFriends = mutableListOf<FriendData>()

        firestore.collection("users").document(currentUserId).get()
            .addOnSuccessListener { userDoc ->
                if (userDoc.exists()) {
                    val currentUser = FriendData(
                        uid = currentUserId,
                        userName = userDoc.getString("userName") ?: "",
                        firstName = userDoc.getString("firstName") ?: "",
                        lastName = userDoc.getString("lastName") ?: "",
                        photo = userDoc.getString("photo") ?: ""
                    )
                    allFriends.add(currentUser)
                }

                firestore.collection("users")
                    .document(currentUserId)
                    .collection("playerFriends")
                    .get()
                    .addOnSuccessListener { documents ->
                        val friendIds = documents.map { it.id }

                        if (friendIds.isEmpty()) {
                            _friendsList.value = allFriends
                            _isLoadingFriends.value = false
                        }

                        var fetchedCount = 0
                        friendIds.forEach { friendId ->
                            firestore.collection("users")
                                .document(friendId)
                                .get()
                                .addOnSuccessListener { doc ->
                                    if (doc.exists()) {
                                        val friend = FriendData(
                                            uid = friendId,
                                            userName = doc.getString("userName") ?: "",
                                            firstName = doc.getString("firstName") ?: "",
                                            lastName = doc.getString("lastName") ?: "",
                                            photo = doc.getString("photo") ?: ""
                                        )
                                        allFriends.add(friend)
                                    }

                                    fetchedCount++
                                    if (fetchedCount == friendIds.size) {
                                        _friendsList.value = allFriends
                                        _isLoadingFriends.value = false
                                    }
                                }
                                .addOnFailureListener {
                                    fetchedCount++
                                    Log.e("FriendsFetch", "Failed to fetch friend data", it)
                                    if (fetchedCount == friendIds.size) {
                                        _friendsList.value = allFriends
                                        _isLoadingFriends.value = false
                                    }
                                }
                        }
                    }
                    .addOnFailureListener {
                        _friendsList.value = allFriends
                        _isLoadingFriends.value = false
                        Log.e("FriendsFetch", "Error fetching friend list", it)
                    }
            }
            .addOnFailureListener {
                _friendsList.value = emptyList()
                _isLoadingFriends.value = false
                Log.e("UserFetch", "Error fetching current user data", it)
            }
    }

    fun selectFriendAt(index: Int, friend: FriendData) {
        val mutableList = _selectedFriends.value.toMutableList()
        if (index in 0..3) {
            mutableList[index] = friend
            _selectedFriends.value = mutableList
        }
    }

    fun unselectFriendAt(index: Int) {
        val mutableList = _selectedFriends.value.toMutableList()
        if (index in 0..3) {
            mutableList[index] = null
            _selectedFriends.value = mutableList
        }
    }

    fun saveMatchDetails(selectedCourt: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val selected = _selectedFriends.value
        val formattedDate = SimpleDateFormat("EEEE, dd MMMM yyyy - HH:mm", Locale.getDefault()).format(Date())
        val matchUrl = "https://res.cloudinary.com/dqcgb73mf/raw/upload/v1746590030/mrfmsyyqaofg1ntdqvdm.json"

        if (currentUser == null || selected.any { it == null }) {
            onFailure(Exception("Invalid data: Check authentication or friend selection."))
            return
        }

        // Ensure consistent order: 4 selected friends
        val orderedPlayerIds = selected.mapNotNull { it?.uid }

        val matchData = hashMapOf(
            "players" to orderedPlayerIds,
            "court" to selectedCourt,
            "timestamp" to System.currentTimeMillis(),
            "formattedTime" to formattedDate,
            "matchUrl" to matchUrl
        )

        val batch = FirebaseFirestore.getInstance().batch()
        val matchId = FirebaseFirestore.getInstance().collection("temp").document().id

        orderedPlayerIds.forEach { uid ->
            val docRef = FirebaseFirestore.getInstance()
                .collection("users").document(uid)
                .collection("uploadedMatches").document(matchId)
            batch.set(docRef, matchData + mapOf("matchId" to matchId))
        }

        batch.commit()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }
}