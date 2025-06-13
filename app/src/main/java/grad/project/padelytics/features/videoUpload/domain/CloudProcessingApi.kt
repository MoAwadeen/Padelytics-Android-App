package grad.project.padelytics.features.videoUpload.domain

import grad.project.padelytics.features.videoUpload.data.ResultResponse
import grad.project.padelytics.features.videoUpload.data.VideoProcessingRequest
import grad.project.padelytics.features.videoUpload.data.VideoProcessingResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface CloudProcessingApi {
    @POST("input-video/")
    suspend fun processVideo(
        @Body request: VideoProcessingRequest
    ): Response<VideoProcessingResponse>

    @GET("result/{id}")
    suspend fun getResult(
        @Path("id") id: String
    ): Response<ResultResponse>
}