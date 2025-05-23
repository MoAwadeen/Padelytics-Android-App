package grad.project.padelytics.features.videoUpload.data

data class VideoProcessingRequest(
    val video_url: String,
    val destination_dir: String = "Uploads/videos",
    val filename: String = "uploaded_video.mp4"
)

data class VideoProcessingResponse(
    val filepath: String
)
