package grad.project.padelytics.features.shop.data.remote.network

import grad.project.padelytics.features.shop.data.remote.model.ProductsList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface AmazonApiService {
    @GET("search")
    suspend fun searchProducts(
        @Header("X-RapidAPI-Key") apiKey: String,
        @Header("X-RapidAPI-Host") host: String = "real-time-amazon-data.p.rapidapi.com",
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("country") country: String = "US"
    ): Response<ProductsList>
}
