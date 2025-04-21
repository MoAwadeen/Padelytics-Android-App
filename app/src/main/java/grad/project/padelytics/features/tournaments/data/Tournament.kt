package grad.project.padelytics.features.tournaments.data

import com.google.firebase.firestore.PropertyName

data class Tournament(
    val tournamentName: String = "",
    val image: String = "",
    val location: String = "",
    val prize: String = "",
    @PropertyName("registrationfees") val registrationFees: String = "",
    val date: String = "",
    val type: String = "",
    val url: String = "",
    val id: String = ""
)
