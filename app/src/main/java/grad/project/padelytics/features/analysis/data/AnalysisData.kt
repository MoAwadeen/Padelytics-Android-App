package grad.project.padelytics.features.analysis.data

data class FullAnalysisData(
    val trajectories: Map<String, TrajectoryData>,
    val distance_total: Map<String, Float>,
    val distance_avg_per_frame: Map<String, Float>,
    val average_speed: Map<String, Float>,
    val max_speed: Map<String, Float>,
    val average_acceleration: Map<String, Float>,
    val zone_presence_percentages: Map<String, Map<String, Float>>,
    val radar_performance: RadarPerformance,
    val ball_trajectory: BallTrajectory,
    val ball_speed_over_time: BallSpeedOverTime,
    val hit_count_per_player: Map<String, Int>,
    val ball_hit_locations: Map<String, PlayerHitLocations>,
    val top_2_strongest_hits: List<StrongestHitItem>,
    val heatmaps: Map<String, PlayerHeatmap>,
    val animation: List<AnimationFrame>
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

data class MetricValues(
    val distanceTotal: Map<String, Float>,
    val distanceAvgPerFrame: Map<String, Float>,
    val averageSpeed: Map<String, Float>,
    val maxSpeed: Map<String, Float>,
    val averageAcceleration: Map<String, Float>,
    val zonePresencePercentages: Map<String, Map<String, Float>>
)

data class RadarPerformance(
    val metrics: List<String>,
    val players: Map<String, Map<String, Float>>
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
    val player: Int,
    val speed: Float
)