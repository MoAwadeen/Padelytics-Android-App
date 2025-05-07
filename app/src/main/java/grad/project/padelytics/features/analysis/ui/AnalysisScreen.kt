package grad.project.padelytics.features.analysis.ui

import android.content.Context
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import grad.project.padelytics.appComponents.FetchingIndicator
import grad.project.padelytics.features.analysis.components.AnalysisAppbar
import grad.project.padelytics.features.analysis.components.AnalysisWideGreenButton
import grad.project.padelytics.features.analysis.components.BallAnalysisBox
import grad.project.padelytics.features.analysis.components.BallHitLocationsPlot
import grad.project.padelytics.features.analysis.components.BallSpeedOverTimeLineChart
import grad.project.padelytics.features.analysis.components.BallTrajectoryPlot
import grad.project.padelytics.features.analysis.components.CourtBackground
import grad.project.padelytics.features.analysis.components.HitCountBarChart
import grad.project.padelytics.features.analysis.components.MatchAnimationCard
import grad.project.padelytics.features.analysis.components.PlayerAnalysisCard
import grad.project.padelytics.features.analysis.components.PlayersView
import grad.project.padelytics.features.analysis.components.RectangleBackground
import grad.project.padelytics.features.analysis.components.TopStrongestHitsBarChart
import grad.project.padelytics.features.analysis.viewModel.AnalysisViewModel

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun AnalysisScreen(modifier: Modifier = Modifier, navController: NavHostController, viewModel: AnalysisViewModel = viewModel()) {
    val context = LocalContext.current
    val composeViewRef = remember { mutableStateOf<ComposeView?>(null) }
    val matchId = remember {
        context.getSharedPreferences("match_prefs", Context.MODE_PRIVATE).getString("match_id", null)
    }
    //val match by viewModel.matchData.collectAsState()
    val playerList by viewModel.players.collectAsState()
    val playerFirstNames by viewModel.playerFirstNames.collectAsState()
    val playerPhotos by viewModel.playerPhotos.collectAsState()
    val playerLevels by viewModel.playerLevels.collectAsState()
    val analysisData by viewModel.analysisData.collectAsState()

    LaunchedEffect(matchId) {
        if (matchId != null) {
            viewModel.fetchMatchById(matchId)
        }
    }

    val playerNames = remember(playerList) {
        playerList.mapIndexed { index, player ->
            "player${index + 1}" to player.firstName
        }.toMap()
    }

    BackHandler {
        navController.popBackStack()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            item {
                if (playerList.size < 4 || analysisData == null) {
                    Column(modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center){
                        FetchingIndicator(modifier = Modifier.fillMaxSize(), isFetching = true)
                    }
                } else {
                    AndroidView(
                        factory = {
                            ComposeView(it).apply {
                                setContent {
                                    Column(
                                        modifier = Modifier.background(Color.White),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        AnalysisAppbar()

                                        PlayersView(players = playerList)

                                        Spacer(modifier = Modifier.height(20.dp))

                                        Column(
                                            modifier = Modifier
                                                .background(Color.White)
                                                .padding(horizontal = 16.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            BallAnalysisBox(
                                                graphScreens = listOf(
                                                    "Ball Trajectory" to @Composable {
                                                        CourtBackground {
                                                            BallTrajectoryPlot(
                                                                ballTrajectory = analysisData!!.ball_trajectory
                                                            )
                                                        }
                                                    },
                                                    "Ball Speed Timeline" to @Composable {
                                                        RectangleBackground {
                                                            BallSpeedOverTimeLineChart(
                                                                data = analysisData!!.ball_speed_over_time
                                                            )
                                                        }
                                                    },
                                                    "Top Speeds" to @Composable {
                                                        RectangleBackground {
                                                            TopStrongestHitsBarChart(
                                                                topHits = analysisData!!.top_3_strongest_hits,
                                                                playerName = playerNames
                                                            )
                                                        }
                                                    },
                                                    "Number Of Ball Hits" to @Composable {
                                                        RectangleBackground {
                                                            HitCountBarChart(
                                                                hitCount = analysisData!!.hit_count_per_player,
                                                                playerDisplayNames = playerNames
                                                            )
                                                        }
                                                    },
                                                    "Ball Hits Locations" to @Composable {
                                                        CourtBackground {
                                                            BallHitLocationsPlot(ballHits = analysisData!!.ball_hit_locations)
                                                        }
                                                    }
                                                )
                                            )

                                            Spacer(modifier = Modifier.height(20.dp))

                                            MatchAnimationCard(
                                                analysisData = analysisData!!,
                                                playerName = playerNames
                                            )

                                            Spacer(modifier = Modifier.height(20.dp))

                                            PlayerAnalysisCard(
                                                analysisData = analysisData!!,
                                                playerList = listOf("player1", "player2", "player3", "player4"),
                                                playerFirstNames = playerFirstNames,
                                                playerPhotos = playerPhotos,
                                                playerLevels = playerLevels
                                            )
                                        }
                                    }
                                }
                                composeViewRef.value = this
                            }
                        }
                    )

                    Column(
                        modifier = Modifier
                            .background(Color.White)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AnalysisWideGreenButton(onClick = {
                            composeViewRef.value?.let {
                                val bitmap = viewModel.captureComposableToBitmap(it)
                                viewModel.saveBitmapAsPdf(context, bitmap)
                            }
                        })
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Preview(showBackground = true)
@Composable
fun AnalysisScreenPreview() {
    AnalysisScreen(navController = rememberNavController())
}