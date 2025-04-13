package grad.project.padelytics.features.shop.data.remote.model

data class ProductsData(
    val total_products: Int,
    val country: String,
    val domain: String,
    val products: List<Product>
)

data class Product(
    val asin: String,
    val product_title: String,
    val product_price: String,
    val product_original_price: String,
    val currency: String,
    val product_star_rating: String?,
    val product_num_ratings: Int?,
    val product_url: String,
    val product_photo: String,
    val product_num_offers: Int,
    val product_minimum_offer_price: String,
    val is_best_seller: Boolean,
    val is_amazon_choice: Boolean,
    val is_prime: Boolean,
    val product_availability: String,
    val climate_pledge_friendly: Boolean,
    val sales_volume: String,
    val delivery: String?,
    val has_variations: Boolean
)
