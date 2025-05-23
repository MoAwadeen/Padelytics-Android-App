package grad.project.padelytics.features.videoUpload.domain

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.MINUTES) // For long video processing
        .writeTimeout(20, TimeUnit.MINUTES)
        .build()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://35.225.232.204/") // Use HTTP temporarily
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val cloudApi: CloudProcessingApi by lazy {
        retrofit.create(CloudProcessingApi::class.java)
    }
}