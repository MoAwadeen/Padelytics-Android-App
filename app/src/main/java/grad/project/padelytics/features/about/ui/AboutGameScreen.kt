package grad.project.padelytics.features.about.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import grad.project.padelytics.R
import grad.project.padelytics.features.about.components.InfoScroller
import grad.project.padelytics.features.about.data.InfoPage
import grad.project.padelytics.features.about.viewModel.AboutViewModel
import grad.project.padelytics.ui.theme.Blue

@Composable
fun AboutGameScreen(modifier: Modifier = Modifier, navController: NavHostController, viewModel: AboutViewModel = viewModel()) {
    val pages = listOf(
        InfoPage(
            image = R.drawable.player,
            description = "Padel has become one of the most significant contemporary sports, with rapid global growth. According to the International Padel Federation (FIP), around 30 million people now play the sport in more than 130 countries."
        ),
        InfoPage(
            image = R.drawable.wide_court,
            description = "Padel’s global infrastructure has expanded significantly, with over 63,000 courts & 20,000 facilities. In Egypt it has seen a remarkable surge, particularly in major cities, with the number of clubs and facilities exceed 200 by 2023 marking a 50% increase from previous years."
        ),
        InfoPage(
            image = R.drawable.courtns,
            description = "Padel is played in pairs on a smaller, enclosed court surrounded by glass walls, blending elements of tennis and squash. Its doubles format boosts teamwork, while the walls add a squash-like dynamic to the tennis-style gameplay."
        ),
        InfoPage(image = R.drawable.rackets,
            description = "Padel uses the same scoring as tennis: 15, 30, 40, and game. Matches are typically best of three sets. The ball bounces once before hitting the glass walls, and players can use the walls during rallies. Serves are underhand and must land in the diagonal service box. Points are lost if the ball bounces twice, hits a player, or goes out without touching the walls."
        )
    )

    BackHandler(enabled = true) { }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Blue)
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Spacer(modifier = Modifier.height(20.dp))

            InfoScroller(pages = pages, onFinished = { navController.popBackStack() })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AboutGameScreenPreview() {
    AboutGameScreen(navController = rememberNavController())
}