package grad.project.padelytics.features.shop.data.remote.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ShopRetrofitModule {
    private const val BASE_URL = "https://real-time-amazon-data.p.rapidapi.com/"
    private const val API_KEY = "f7f71cf292msha1eb101ed45cee4p176c8fjsndc395517831a"
    //private const val API_KEY = "bf05750701msh9cbccdf32ea1e7cp1858fdjsn56e647c0b202"

    val apiService: AmazonApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AmazonApiService::class.java)
    }

    fun getApiKey(): String = API_KEY
}