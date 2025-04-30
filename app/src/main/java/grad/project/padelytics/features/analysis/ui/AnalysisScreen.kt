package grad.project.padelytics.features.analysis.ui

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import grad.project.padelytics.features.analysis.components.AnalysisAppbar
import grad.project.padelytics.features.analysis.components.AnalysisWideGreenButton
import grad.project.padelytics.features.analysis.components.BallAnalysisBox
import grad.project.padelytics.features.analysis.components.PlayersView
import grad.project.padelytics.features.analysis.data.FullAnalysisData
import grad.project.padelytics.features.analysis.data.MetricValues
import grad.project.padelytics.features.analysis.viewModel.AnalysisViewModel

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun AnalysisScreen(modifier: Modifier = Modifier, navController: NavHostController, viewModel: AnalysisViewModel = viewModel()) {
    val context = LocalContext.current
    val composeViewRef = remember { mutableStateOf<ComposeView?>(null) }
    val jsonString = viewModel.loadJsonFromAssets(context = context, filename = "players_and_ball_visualization_data.json")
    val gson = Gson()
    val type = object : TypeToken<FullAnalysisData>() {}.type
    val analysisData: FullAnalysisData = gson.fromJson(jsonString, type)

    val playersData = mapOf(
        "player1" to analysisData.trajectories["player1"]!!,
        "player2" to analysisData.trajectories["player2"]!!,
        "player3" to analysisData.trajectories["player3"]!!,
        "player4" to analysisData.trajectories["player4"]!!
    )

    val metricData = MetricValues(
        distanceTotal = analysisData.distance_total,
        distanceAvgPerFrame = analysisData.distance_avg_per_frame,
        averageSpeed = analysisData.average_speed,
        maxSpeed = analysisData.max_speed,
        averageAcceleration = analysisData.average_acceleration,
        zonePresencePercentages = analysisData.zone_presence_percentages
    )

    BackHandler {
        navController.popBackStack()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                AndroidView(
                    factory = {
                        ComposeView(it).apply {
                            setContent {
                                Column(
                                    modifier = Modifier
                                        .background(Color.White),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    AnalysisAppbar()

                                    PlayersView()

                                    Spacer(modifier = Modifier.height(20.dp))

                                    Column(
                                        modifier = Modifier
                                            .background(Color.White)
                                            .padding(horizontal = 16.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        BallAnalysisBox()
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
                        val bitmap = viewModel.captureComposableToBitmap(composeViewRef.value!!)
                        viewModel.saveBitmapAsPdf(context, bitmap)
                    })
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