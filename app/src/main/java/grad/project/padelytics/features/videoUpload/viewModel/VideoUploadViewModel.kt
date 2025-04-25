package grad.project.padelytics.features.videoUpload.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import grad.project.padelytics.features.videoUpload.data.FriendData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class VideoUploadViewModel : ViewModel() {

    private val _selectedVideoUri = MutableStateFlow<Uri?>(null)
    val selectedVideoUri: StateFlow<Uri?> = _selectedVideoUri

    private val _searchResult = MutableStateFlow<Pair<String, String>?>(null)
    val searchResult: StateFlow<Pair<String, String>?> = _searchResult

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _friendsList = MutableStateFlow<List<FriendData>>(emptyList())
    val friendsList: StateFlow<List<FriendData>> = _friendsList

    private val _isLoadingFriends = MutableStateFlow(false)
    val isLoadingFriends: StateFlow<Boolean> = _isLoadingFriends

    private val _selectedFriend = MutableStateFlow<FriendData?>(null)
    val selectedFriend: StateFlow<FriendData?> = _selectedFriend


    private val _isFriendSelected = MutableStateFlow(false)
    val isFriendSelected: StateFlow<Boolean> = _isFriendSelected

    private val firestore = FirebaseFirestore.getInstance()

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
        firestore.collection("users")
            .document(currentUserId)
            .collection("playerFriends")
            .get()
            .addOnSuccessListener { documents ->
                val friends = documents.mapNotNull { doc ->
                    FriendData(
                        userName = doc.getString("userName") ?: "",
                        firstName = doc.getString("firstName") ?: "",
                        lastName = doc.getString("lastName") ?: "",
                        photo = doc.getString("photo") ?: ""
                    )
                }
                _friendsList.value = friends
                _isLoadingFriends.value = false
            }
            .addOnFailureListener {
                _isLoadingFriends.value = false
                Log.e("FriendsFetch", "Error fetching friends", it)
            }
    }

    fun selectFriend(friend: FriendData) {
        _selectedFriend.value = friend
        _isFriendSelected.value = true
        Log.d("VideoUploadVM", "Friend selected: ${friend.userName}")
    }

    fun clearSelectedFriend() {
        _selectedFriend.value = null
    }
}