package grad.project.padelytics.data

data class UserModel(
    val firstName: String,
    val lastName: String,
    val uid: String = "",
    val email: String,
    val password: String,
    val photo: String
)

data class UserProfileModel(
    val uid: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val photo: String = "",
    val wins: Int = 0,
    val losses: Int = 0,
    val rank: Int = 0,
    val rewardPoints: Int = 0,
    val city: String = "",
    val confidence: Double = 0.0
)



data class UserModelExtra(
    val uid: String = "",
    val gender: String,
    val level: String,
    val date: String,
    val city: String,
)
