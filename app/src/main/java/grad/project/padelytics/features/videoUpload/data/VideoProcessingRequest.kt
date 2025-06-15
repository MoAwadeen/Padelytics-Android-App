package grad.project.padelytics.features.videoUpload.data

data class ResultResponse(
    val status: String,
    val url: String?
)

data class VideoProcessingRequest(
    val video_url: String,
    val destination_dir: String = "Uploads/videos",
    val filename: String = "uploaded_video.mp4"
)

data class VideoProcessingResponse(
    val status: String,
    val video_id: String
)