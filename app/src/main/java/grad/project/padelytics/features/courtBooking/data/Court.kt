package grad.project.padelytics.features.courtBooking.data

data class Court(
    val courtId:String = "",
    val courtName: String = "",
    val courtLocation: String = "",
    val courtOnMap: String = "",
    val bookingUrl: String = "",
    val courtImage: String = "",
    val courtCity: String = "",
    val courtRating: String = "",
    val numRating: String = "",
    val instagramPage: String = "",
    val numPlayers: String = "",
    val bookingPrice: String = "",
    val firstPhoto: String = "",
    val secondPhoto: String = "",
    val twoPlayers: Boolean = false,
    val fourPlayers: Boolean = true
)