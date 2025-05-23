package grad.project.padelytics.features.videoUpload.domain

import grad.project.padelytics.features.videoUpload.data.VideoProcessingRequest
import grad.project.padelytics.features.videoUpload.data.VideoProcessingResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface CloudProcessingApi {
    @POST("input-video/")
    suspend fun processVideo(
        @Body request: VideoProcessingRequest
    ): Response<VideoProcessingResponse>
}
