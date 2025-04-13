package grad.project.padelytics.features.shop.data.remote.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ShopRetrofitModule {
    private const val BASE_URL = "https://real-time-amazon-data.p.rapidapi.com/"
    private const val API_KEY = "c6948ac65fmsh6265da08a3d340dp114c64jsna77e78c8551e"

    val apiService: AmazonApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AmazonApiService::class.java)
    }

    fun getApiKey(): String = API_KEY
}
