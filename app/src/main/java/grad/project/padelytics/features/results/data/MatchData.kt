package grad.project.padelytics.features.results.data

data class PlayerInfo(
    val id: String,
    val firstName: String,
    val photo: String,
    val level: String
)

data class MatchData(
    val players: List<PlayerInfo>,
    val court: String,
    val formattedTime: String,
    val timestamp: Number,
    val matchId: String,
    val matchUrl: String
)