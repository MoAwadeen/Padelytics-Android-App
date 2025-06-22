package grad.project.padelytics.features.shop.viewModel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import grad.project.padelytics.features.shop.data.remote.model.Product
import grad.project.padelytics.features.shop.data.remote.network.ShopRetrofitModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ShopViewModel : ViewModel() {
    private val _products = mutableStateOf<List<Product>>(emptyList())
    val products: State<List<Product>> = _products
    private val _selectedCategory = mutableStateOf("All")
    val selectedCategory: State<String> = _selectedCategory
    private val _selectedBrand = mutableStateOf("All")
    val selectedBrand: State<String> = _selectedBrand
    private val _selectedSorting = mutableStateOf("Featured")
    val selectedSorting: State<String> = _selectedSorting
    private val _isFetching = MutableStateFlow(false)
    val isFetching: StateFlow<Boolean> = _isFetching.asStateFlow()

    private val brandSynonyms = mapOf(
        "Adidas" to listOf("Adidas", "اديداس", "أديداس"),
        "Nike" to listOf("Nike", "نايك"),
        "Puma" to listOf("Puma", "بوما"),
        "Head" to listOf("Head", "هيد"),
        "Wilson" to listOf("Wilson", "ويلسون"),
        "Asics" to listOf("Asics", "اسيكس"),
        "NOX" to listOf("NOX", "نوكس"),
        "Bullpadel" to listOf("Bullpadel", "بولبادل"),
        "Babolat" to listOf("Babolat", "بابولات"),
        "Star vie" to listOf("Star vie", "StarVie" , "ستار في" ,"ستارفي"),
        "Tecnifibre" to listOf("Tecnifibre", "تكنيفايبر", "تكنيفيبر"),
        "Qshop" to listOf("Qshop", "كيو شوب"),
        "S SIUX" to listOf("S SIUX", "سيوكس"),
        "Camewin" to listOf("Camewin", "كاموين"),
        "Dunlop Sports" to listOf("Dunlop Sports"),
        "Stiga" to listOf("Stiga"),
        "Tourna" to listOf("Tourna"),
        "Harrow" to listOf("Harrow"),
        "Gamma" to listOf("Gamma"),
        "Cosmos" to listOf("Cosmos"),
        "Franklin" to listOf("Franklin"),
        "Ytonet" to listOf("Ytonet"),
        "Incro" to listOf("Incro"),
        "RYZE" to listOf("RYZE"),
        "WLSRW" to listOf("WLSRW"),
        "Generic" to listOf("Generic")
    )

    init {
        searchProducts("padel")
    }

    fun detectBrandFromMultiLangTitle(title: String): String {
        val lowerTitle = title.lowercase()
        for ((brand, synonyms) in brandSynonyms) {
            for (synonym in synonyms) {
                if (lowerTitle.contains(synonym.lowercase())) {
                    return synonym
                }
            }
        }
        return "Unknown"
    }

    fun updateFilters(category: String, brand: String, sort_by: String) {
        _selectedCategory.value = category
        _selectedBrand.value = brand
        _selectedSorting.value = sort_by
        val query = buildQuery(category, brand, sort_by)
        searchProducts(query)
    }

    private fun buildQuery(category: String, brand: String, sort_by: String): String {
        return buildString {
            append("padel")
            if (category != "All") append(" $category")
            if (brand != "All") append(" $brand")
            if (sort_by != "Featured") append(" $sort_by")
        }
    }

    private fun parsePrice(price: String): Double {
        return price
            .replace(Regex(pattern = "[^\\d.]"), replacement = "")
            .toDoubleOrNull() ?: Double.MAX_VALUE
    }

    private fun sortProducts(products: List<Product>, sortOption: String): List<Product> {
        return when (sortOption) {
            "Price: Low to High" -> products.sortedBy {
                parsePrice(it.product_price)
            }
            "Price: High to Low" -> products.sortedByDescending {
                parsePrice(it.product_price)
            }
            "Avg. Customer Review" -> products.sortedByDescending {
                it.product_star_rating?.toDoubleOrNull() ?: 0.0
            }
            "Featured" -> products
            "Best Sellers" -> products.filter { it.is_best_seller }
            else -> products
        }
    }

    private fun searchProducts(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isFetching.value = true
            try {
                val response = ShopRetrofitModule.apiService.searchProducts(
                    apiKey = ShopRetrofitModule.getApiKey(),
                    query = query
                )

                if (response.isSuccessful) {
                    val productList = response.body()?.data?.products ?: emptyList()
                    val sortedProducts = sortProducts(productList, _selectedSorting.value)
                    _products.value = sortedProducts
                    //.shuffled().take(10)
                    _isFetching.value = false
                } else {
                    Log.e("ShopViewModel", "API Request Failed: ${response.message()}")
                    _isFetching.value = false
                }
            } catch (e: Exception) {
                Log.e("ShopViewModel", "Error: ${e.message}")
            }
        }
    }

    fun saveFavoriteProduct(
        productId: String,
        productName: String,
        productPrice: String,
        productImage: String,
        productUrl: String,
        productRating: String,
        productNumRating: String,
        productDelivery: String,
        productOffers: String,
        productBrand: String,
        onComplete: (Boolean) -> Unit
    ) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val db = FirebaseFirestore.getInstance()
            val favoriteProductRef = db.collection("users")
                .document(userId)
                .collection("favoriteProducts")
                .document(productId)

            val favoriteData = hashMapOf(
                "productId" to productId,
                "productName" to productName,
                "productPrice" to productPrice,
                "productImage" to productImage,
                "productUrl" to productUrl,
                "productRating" to productRating,
                "productNumRating" to productNumRating,
                "productDelivery" to productDelivery,
                "productOffers" to productOffers,
                "productBrand" to productBrand
            )
            favoriteProductRef.set(favoriteData)
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

    fun removeFavoriteProduct(productId: String, onComplete: (Boolean) -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val db = FirebaseFirestore.getInstance()
            db.collection("users")
                .document(userId)
                .collection("favoriteProducts")
                .document(productId)
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

    fun checkIfFavorite(productId: String, onResult: (Boolean) -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val db = FirebaseFirestore.getInstance()
            db.collection("users")
                .document(userId)
                .collection("favoriteProducts")
                .document(productId)
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