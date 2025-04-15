package grad.project.padelytics.data

data class UserModel(
    val firstName: String,
    val lastName: String,
    val username: String = "",
    val uid: String = "",
    val email: String,
    val password: String,
    val photo: String
)

data class UserProfileModel(
    val uid: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val userName: String = "",
    val email: String = "",
    val photo: String = "",
    val wins: Int = 0,
    val losses: Int = 0,
    val rank: Int = 0,
    val rewardPoints: Int = 0,
    val city: String = "",
    val confidence: Double = 0.0,
)

