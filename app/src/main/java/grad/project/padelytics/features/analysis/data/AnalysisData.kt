package grad.project.padelytics.features.analysis.data

data class FullAnalysisData(
    val trajectories: Map<String, TrajectoryData> = emptyMap(),
    val distance_total: Map<String, Float> = emptyMap(),
    val distance_avg_per_frame: Map<String, Float> = emptyMap(),
    val average_speed: Map<String, Float> = emptyMap(),
    val max_speed: Map<String, Float> = emptyMap(),
    val average_acceleration: Map<String, Float> = emptyMap(),
    val zone_presence_percentages: Map<String, Map<String, Float>> = emptyMap(),
    val radar_performance: RadarPerformance,
    val ball_trajectory: BallTrajectory,
    val ball_speed_over_time: BallSpeedOverTime,
    val hit_count_per_player: Map<String, Int> = emptyMap(),
    val ball_hit_locations: Map<String, PlayerHitLocations> = emptyMap(),
    val top_3_strongest_hits: List<StrongestHitItem>,
    val heatmaps: Map<String, PlayerHeatmap> = emptyMap(),
    val animation: List<AnimationFrame>,
    val role: Map<String, String> = emptyMap(),
    val role_advice: Map<String, String> = emptyMap(),
    val reaction_time_efficiency: Map<String, Float?> = emptyMap(),
    val reaction_advice: Map<String, String> = emptyMap(),
    val shot_effectiveness: Map<String, Float?> = emptyMap(),
    val shot_advice: Map<String, String> = emptyMap(),
    val team_hits: Map<String, Int?> = emptyMap(),
    val player_contribution: Map<String, Float?> = emptyMap(),
    val player_contribution_advice: Map<String, String> = emptyMap(),
    val stamina_drop_time: Map<String, Float?> = emptyMap(),
    val stamina_advice: Map<String, String> = emptyMap(),
    )

data class TrajectoryData(
    val x: List<Float?>,
    val y: List<Float?>
)

data class AnimationFrame(
    val Frame: Int,
    val player1: PlayerXY,
    val player2: PlayerXY,
    val player3: PlayerXY,
    val player4: PlayerXY
)

data class PlayerXY(
    val x: Float,
    val y: Float
)

data class PlayerHeatmap(
    val x: List<Float>,
    val y: List<Float>
)

data class RadarPerformance(
    val metrics: List<String>,
    val players: Map<String, Map<String, Float>> = emptyMap()
)

data class BallTrajectory(
    val x: List<Float>,
    val y: List<Float>
)

data class BallSpeedOverTime(
    val frame: List<Float>,
    val speed: List<Float>
)

data class PlayerHitLocations(
    val x: List<Float>,
    val y: List<Float>
)

data class StrongestHitItem(
    val player: String,
    val speed: Float
)