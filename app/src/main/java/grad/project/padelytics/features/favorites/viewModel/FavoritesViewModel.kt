package grad.project.padelytics.features.favorites.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import grad.project.padelytics.features.favorites.data.FavoriteCourt
import grad.project.padelytics.features.favorites.data.FavoriteProduct
import grad.project.padelytics.features.favorites.data.FavoriteTournament
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoritesViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val _tournaments = MutableStateFlow<List<FavoriteTournament>>(emptyList())
    val favoriteTournaments: StateFlow<List<FavoriteTournament>> = _tournaments.asStateFlow()
    private val _courts = MutableStateFlow<List<FavoriteCourt>>(emptyList())
    val favoriteCourts: StateFlow<List<FavoriteCourt>> = _courts.asStateFlow()
    private val _products = MutableStateFlow<List<FavoriteProduct>>(emptyList())
    val favoriteProducts: StateFlow<List<FavoriteProduct>> = _products.asStateFlow()
    private val _isFetching = MutableStateFlow(false)
    val isFetching: StateFlow<Boolean> = _isFetching.asStateFlow()

    fun fetchFavoriteTournaments(userId: String) {
        viewModelScope.launch {
            _isFetching.value = true
            firestore.collection("users")
                .document(userId)
                .collection("favoriteTournaments")
                .get()
                .addOnSuccessListener { result ->
                    val favoriteList = result.map { document ->
                        FavoriteTournament(
                            tournamentName = document.getString("tournamentName") ?: "",
                            image = document.getString("image") ?: "",
                            location = document.getString("location") ?: "",
                            prize = document.getString("prize") ?: "",
                            url = document.getString("url") ?: "",
                            id = document.getString("id") ?: ""
                        )
                    }
                    _tournaments.value = favoriteList
                    _isFetching.value = false
                }
                .addOnFailureListener { e ->
                    e.printStackTrace()
                    _isFetching.value = false
                }
        }
    }

    fun fetchFavoriteCourts(userId: String) {
        viewModelScope.launch {
            _isFetching.value = true
            firestore.collection("users")
                .document(userId)
                .collection("favoriteCourts")
                .get()
                .addOnSuccessListener { result ->
                    val favoriteList = result.map { document ->
                        FavoriteCourt(
                            courtName = document.getString("courtName") ?: "",
                            courtImage = document.getString("courtImage") ?: "",
                            bookingPrice = document.getString("bookingPrice") ?: "",
                            courtCity = document.getString("courtCity") ?: "",
                            courtRating = document.getString("courtRating") ?: "",
                            courtId = document.getString("courtId") ?: "",
                            firstPhoto = document.getString("firstPhoto") ?: "",
                            numPlayers = document.getString("numPlayers") ?: "",
                            numRating = document.getString("numRating") ?: "",
                            secondPhoto = document.getString("secondPhoto") ?: ""
                        )
                    }
                    _courts.value = favoriteList
                    _isFetching.value = false
                }
                .addOnFailureListener { e ->
                    e.printStackTrace()
                    _isFetching.value = false
                }
        }
    }

    fun fetchFavoriteProducts(userId: String) {
        viewModelScope.launch {
            _isFetching.value = true
            firestore.collection("users")
                .document(userId)
                .collection("favoriteProducts")
                .get()
                .addOnSuccessListener { result ->
                    val favoriteList = result.map { document ->
                        FavoriteProduct(
                            productImage = document.getString("productImage") ?: "",
                            productName = document.getString("productName") ?: "",
                            productPrice = document.getString("productPrice") ?: "",
                            productRating = document.getString("productRating") ?: "",
                            productUrl = document.getString("productUrl") ?: "",
                            productId = document.getString("productId") ?: "",
                            productNumRating = document.getString("productNumRating") ?: "",
                            productDelivery = document.getString("productDelivery") ?: "",
                            productOffers = document.getString("productOffers") ?: "",
                            productBrand = document.getString("productBrand") ?: ""
                        )
                    }
                    _products.value = favoriteList
                    _isFetching.value = false
                }
                .addOnFailureListener { e ->
                    e.printStackTrace()
                    _isFetching.value = false
                }
        }
    }
}