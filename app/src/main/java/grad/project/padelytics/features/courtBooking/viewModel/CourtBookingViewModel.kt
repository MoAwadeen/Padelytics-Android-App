package grad.project.padelytics.features.courtBooking.viewModel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import grad.project.padelytics.features.courtBooking.data.Court
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class CourtBookingViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val _courts = MutableStateFlow<List<Court>>(emptyList())
    val courts: StateFlow<List<Court>> = _courts
    private val db = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    private val _selectedPlayers = mutableIntStateOf(2)
    val selectedPlayers: State<Int> = _selectedPlayers
    private val _selectedCity = mutableStateOf("All Cities")
    val selectedCity: State<String> = _selectedCity
    private val _isFetching = MutableStateFlow(false)
    val isFetching: StateFlow<Boolean> = _isFetching.asStateFlow()

    fun getCourtById(courtId: String?): Flow<Court?> = flow {
        if (courtId != null) {
            _isFetching.value = true
            try {
                Log.d("CourtBookingViewModel", "Fetching document: $courtId")
                val snapshot = db.collection("courts")
                    .document(courtId)
                    .get()
                    .await()

                if (snapshot.exists()) {
                    val court = snapshot.toObject(Court::class.java)
                    emit(court)
                    _isFetching.value = false
                } else {
                    Log.d("CourtBookingViewModel", "Court not found")
                    emit(null)
                    _isFetching.value = false
                }
            } catch (e: Exception) {
                Log.e("CourtBookingViewModel", "Error loading court", e)
                emit(null)
            }
        }
    }.flowOn(Dispatchers.IO)

    fun saveFavoriteCourt(court: Court, onComplete: (Boolean) -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val db = FirebaseFirestore.getInstance()
            val favoriteCourtRef = db.collection("users")
                .document(userId)
                .collection("favoriteCourts")
                .document(court.courtId)

            val favoriteData = hashMapOf(
                "courtId" to court.courtId,
                "courtName" to court.courtName,
                "courtImage" to court.courtImage,
                "courtCity" to court.courtCity,
                "courtRating" to court.courtRating,
                "numRating" to court.numRating,
                "bookingPrice" to court.bookingPrice,
                "numPlayers" to court.numPlayers,
                "firstPhoto" to court.firstPhoto,
                "secondPhoto" to court.secondPhoto
            )

            favoriteCourtRef.set(favoriteData)
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

    fun removeFavoriteCourt(courtId: String, onComplete: (Boolean) -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val db = FirebaseFirestore.getInstance()
            db.collection("users")
                .document(userId)
                .collection("favoriteCourts")
                .document(courtId)
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

    fun checkIfFavorite(courtId: String, onResult: (Boolean) -> Unit) {
        if (userId != null) {
            db.collection("users")
                .document(userId)
                .collection("favoriteCourts")
                .document(courtId)
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

    fun setSelectedPlayers(count: Int) {
        _selectedPlayers.intValue = count
    }

    fun setSelectedCity(city: String) {
        _selectedCity.value = city
    }

    fun getAllCities(onResult: (List<String>) -> Unit) {
        firestore.collection("courts")
            .get()
            .addOnSuccessListener { result ->
                val cities = result.mapNotNull { it.getString("courtCity") }
                    .distinct()
                    .sorted()
                onResult(cities)
            }
    }

    fun fetchFilteredCourts(playerCount: Int) {
        viewModelScope.launch {
            _isFetching.value = true
            val filterField = if (playerCount == 2) "twoPlayers" else "fourPlayers"

            var query = firestore.collection("courts")
                .whereEqualTo(filterField, true)

            if (_selectedCity.value != "All Cities") {
                query = query.whereEqualTo("courtCity", _selectedCity.value)
            }

            query.get()
                .addOnSuccessListener { result ->
                    val courtList = result.map { document ->
                        Court(
                            courtName = document.getString("courtName") ?: "",
                            courtImage = document.getString("courtImage") ?: "",
                            courtLocation = document.getString("courtLocation") ?: "",
                            courtOnMap = document.getString("courtOnMap") ?: "",
                            courtCity = document.getString("courtCity") ?: "",
                            courtId = document.getString("courtId") ?: "",
                            bookingUrl = document.getString("bookingUrl") ?: "",
                            courtRating = document.getString("courtRating") ?: "",
                            numRating = document.getString("numRating") ?: "",
                            instagramPage = document.getString("instagramPage") ?: "",
                            numPlayers = document.getString("numPlayers") ?: "",
                            bookingPrice = document.getString("bookingPrice") ?: "",
                            firstPhoto = document.getString("firstPhoto") ?: "",
                            secondPhoto = document.getString("secondPhoto") ?: "",
                            twoPlayers = document.getBoolean("twoPlayers") ?: false,
                            fourPlayers = document.getBoolean("fourPlayers") ?: true
                        )
                    }
                    _courts.value = courtList
                    _isFetching.value = false
                }
                .addOnFailureListener { e ->
                    e.printStackTrace()
                    _isFetching.value = false
                }
        }
    }
}