package grad.project.padelytics.features.videoUpload.viewModel

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import grad.project.padelytics.features.videoUpload.data.FriendData
import grad.project.padelytics.features.videoUpload.data.VideoProcessingRequest
import grad.project.padelytics.features.videoUpload.domain.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class VideoUploadViewModel(application: Application) : AndroidViewModel(application) {
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

    private val firestore = FirebaseFirestore.getInstance()

    private val _selectedFriends = MutableStateFlow<List<FriendData?>>(listOf(null, null, null, null))
    val selectedFriends: StateFlow<List<FriendData?>> = _selectedFriends

    private val _videoUri = MutableStateFlow<Uri?>(null)
    val videoUri = _videoUri.asStateFlow()

    private var cloudinaryInitialized = false

    private val _resultUrl = MutableStateFlow("")
    val resultUrl: StateFlow<String> = _resultUrl.asStateFlow()

    private val _showResultDialog = MutableStateFlow(false)
    val showResultDialog: StateFlow<Boolean> = _showResultDialog.asStateFlow()

    private val _thumbnailBitmap = MutableStateFlow<Bitmap?>(null)
    val thumbnailBitmap = _thumbnailBitmap.asStateFlow()

    fun setVideo(uri: Uri) {
        _videoUri.value = uri
        extractThumbnail(uri)
    }

    fun setSelectedVideo(uri: Uri) {
        _selectedVideoUri.value = uri
    }

    fun extractThumbnail(uri: Uri) {
        viewModelScope.launch {
            val retriever = MediaMetadataRetriever()
            try {
                retriever.setDataSource(getApplication<Application>(), uri)
                val frame = retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
                _thumbnailBitmap.value = frame
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                retriever.release()
            }
        }
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

        // Step 1: Fetch friend's data
        usersRef.document(friendDocId).get()
            .addOnSuccessListener { friendDoc ->
                if (friendDoc.exists()) {
                    val friendUsername = friendDoc.getString("userName") ?: return@addOnSuccessListener
                    val friendFirstName = friendDoc.getString("firstName") ?: ""
                    val friendLastName = friendDoc.getString("lastName") ?: ""
                    val friendPhoto = friendDoc.getString("photo") ?: ""

                    val friendData = mapOf(
                        "uid" to friendDocId,
                        "userName" to friendUsername,
                        "firstName" to friendFirstName,
                        "lastName" to friendLastName,
                        "photo" to friendPhoto
                    )

                    // Step 2: Add friend to current user's friends list
                    usersRef.document(currentUserId)
                        .collection("playerFriends")
                        .document(friendDocId)
                        .set(friendData)
                        .addOnSuccessListener {
                            Log.d("FriendAdd", "Friend added to current user successfully")
                            fetchFriendsList() // Refresh list

                            // Step 3: Fetch current user's data to reciprocate
                            usersRef.document(currentUserId).get()
                                .addOnSuccessListener { currentUserDoc ->
                                    if (currentUserDoc.exists()) {
                                        val currentUsername = currentUserDoc.getString("userName") ?: ""
                                        val currentFirstName = currentUserDoc.getString("firstName") ?: ""
                                        val currentLastName = currentUserDoc.getString("lastName") ?: ""
                                        val currentPhoto = currentUserDoc.getString("photo") ?: ""

                                        val currentUserData = mapOf(
                                            "uid" to currentUserId,
                                            "userName" to currentUsername,
                                            "firstName" to currentFirstName,
                                            "lastName" to currentLastName,
                                            "photo" to currentPhoto
                                        )

                                        // Step 4: Add current user to friend's friends list
                                        usersRef.document(friendDocId)
                                            .collection("playerFriends")
                                            .document(currentUserId)
                                            .set(currentUserData)
                                            .addOnSuccessListener {
                                                Log.d("FriendAdd", "Current user added to friend's list successfully")
                                            }
                                            .addOnFailureListener { e ->
                                                Log.e("FriendAdd", "Failed to add current user to friend's list", e)
                                            }
                                    }
                                }
                                .addOnFailureListener {
                                    Log.e("CurrentUserFetch", "Could not fetch current user document", it)
                                }
                        }
                        .addOnFailureListener { e ->
                            Log.e("FriendAdd", "Failed to add friend to current user", e)
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

    fun saveMatchDetails(selectedCourt: String, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
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
            .addOnSuccessListener { onSuccess(matchId) }
            .addOnFailureListener { onFailure(it) }
    }


        private fun uriToFile(context: Context, uri: Uri): File {
            val inputStream = context.contentResolver.openInputStream(uri)
            val file = File.createTempFile("upload_", ".mp4", context.cacheDir)
            inputStream.use { input ->
                file.outputStream().use { output ->
                    input?.copyTo(output)
                }
            }
            return file
        }


    fun uploadVideoToCloudinary(context: Context, onResult: (String) -> Unit) {
        val uri = _videoUri.value
        if (uri == null) {
            Log.e("CloudinaryUpload", "No video selected")
            onResult("Error: No video selected")
            return
        }

        _isLoading.value = true

        viewModelScope.launch {
            try {
                if (!cloudinaryInitialized) {
                    val config = hashMapOf(
                        "cloud_name" to "dqcgb73mf",
                        "api_key" to "711966464192934",
                        "api_secret" to "_6CYk7HyN4lF9ZG2NDjWrwAvDAw",
                        "secure" to "true"
                    )
                    MediaManager.init(context.applicationContext, config)
                    cloudinaryInitialized = true
                }

                MediaManager.get().upload(uri)
                    .option("resource_type", "video")
                    .option("folder", "videos")
                    .callback(object : UploadCallback {
                        override fun onStart(requestId: String) {}
                        override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {}
                        override fun onSuccess(requestId: String, resultData: MutableMap<Any?, Any?>) {
                            val secureUrl = resultData["secure_url"] as? String
                            Log.d("CloudinaryUpload", "Upload successful: $secureUrl")
                            onResult("Success: $secureUrl")
                            _isLoading.value = false
                        }

                        override fun onError(requestId: String, error: ErrorInfo) {
                            Log.e("CloudinaryUpload", "Upload failed: ${error.description}")
                            onResult("Error: ${error.description}")
                            _isLoading.value = false
                        }

                        override fun onReschedule(requestId: String, error: ErrorInfo) {
                            Log.e("CloudinaryUpload", "Upload rescheduled: ${error.description}")
                            onResult("Rescheduled: ${error.description}")
                            _isLoading.value = false
                        }
                    })
                    .dispatch()
            } catch (e: Exception) {
                Log.e("CloudinaryUpload", "Exception: ${e.message}", e)
                onResult("Exception: ${e.message}")
                _isLoading.value = false
            }
        }
    }

    fun uploadAndProcessVideo(context: Context, onResult: (String) -> Unit) {
        val uri = _videoUri.value ?: run {
            Log.e("UploadError", "No video selected")
            onResult("Error: No video selected")
            return
        }

        _isLoading.value = true

        viewModelScope.launch {
            try {
                if (!cloudinaryInitialized) {
                    MediaManager.init(context.applicationContext, mapOf(
                        "cloud_name" to "dqcgb73mf",
                        "api_key" to "711966464192934",
                        "api_secret" to "_6CYk7HyN4lF9ZG2NDjWrwAvDAw",
                        "secure" to "true"
                    ))
                    cloudinaryInitialized = true
                }

                MediaManager.get().upload(uri)
                    .option("resource_type", "video")
                    .option("folder", "videos")
                    .callback(object : UploadCallback {
                        override fun onSuccess(requestId: String, resultData: MutableMap<Any?, Any?>) {
                            val secureUrl = resultData["secure_url"] as? String ?: run {
                                Log.e("UploadError", "No URL returned")
                                onResult("Error: No URL returned")
                                _isLoading.value = false
                                return
                            }

                            viewModelScope.launch {
                                try {
                                    val response = RetrofitInstance.cloudApi.processVideo(
                                        VideoProcessingRequest(video_url = secureUrl)
                                    )

                                    if (response.isSuccessful) {
                                        _resultUrl.value = response.body()?.filepath ?: "Analysis completed"
                                        _showResultDialog.value = true
                                    } else {
                                        _resultUrl.value = "Error: ${response.code()}"
                                        _showResultDialog.value = true
                                    }
                                } catch (e: Exception) {
                                    _resultUrl.value = "Error: ${e.message}"
                                    _showResultDialog.value = true
                                } finally {
                                    _isLoading.value = false
                                }
                            }
                        }

                        override fun onError(requestId: String, error: ErrorInfo) {
                            Log.e("UploadError", "Failed: ${error.description}")
                            onResult("Error: ${error.description}")
                            _isLoading.value = false
                        }

                        override fun onStart(requestId: String) {}
                        override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {}
                        override fun onReschedule(requestId: String, error: ErrorInfo) {
                            Log.e("UploadError", "Rescheduled: ${error.description}")
                            onResult("Rescheduled: ${error.description}")
                            _isLoading.value = false
                        }
                    })
                    .dispatch()
            } catch (e: Exception) {
                Log.e("UploadError", "Exception: ${e.message}")
                onResult("Error: ${e.message}")
                _isLoading.value = false
            }
        }
    }

    fun dismissDialog() {
        _showResultDialog.value = false
    }

}