package grad.project.padelytics.data

data class UserModel(
    val firstName: String,
    val lastName: String,
    val uid: String = "",
    val email: String,
    val password: String,
    val photo: String
   // val gender: String,
    //val level: String,
    //val age: Int,
    //val city: String,
)


data class UserModelExtra(
    val uid: String = "",
    val gender: String,
    val level: String,
    val date: String,
    val city: String,
)
