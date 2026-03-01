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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import grad.project.padelytics.R
import grad.project.padelytics.features.about.components.AboutAppScroller
import grad.project.padelytics.features.about.data.InfoPage
import grad.project.padelytics.features.about.viewModel.AboutViewModel
import grad.project.padelytics.features.auth.viewModel.AuthViewModel
import grad.project.padelytics.navigation.Routes
import grad.project.padelytics.ui.theme.Blue

@Composable
fun AboutAppScreen(modifier: Modifier = Modifier, navController: NavHostController, viewModel: AboutViewModel = viewModel()) {

    val pages = listOf(
        InfoPage(
            image = R.drawable.video_uploading2,
            description = "Welcome to Padelytics – the ultimate AI-powered companion for padel players. Just place your phone and start playing, our AI captures every move, From match highlights to deep performance insights, Padelytics turns your game into data you can actually use."
        ),
        InfoPage(
            image = R.drawable.analysis2,
            description = "The Game Broken Down Like Never Before! See your strengths, spot your weaknesses, and track your progress match by match. From shot accuracy to take advices, every detail is visualized, so you can play smarter next time."
        ),
        InfoPage(
            image = R.drawable.booking2,
            description = "Find. Book. Play! No more calls or wait times, discover nearby courts, check availability, and reserve your slot in seconds. Padelytics makes booking as smooth as your backhand."
        ),
        InfoPage(image = R.drawable.shop2,
            description = "Gear Up Like a Pro. Explore top-quality rackets, apparel, and accessories — handpicked for padel players, From beginner to beast mode, we’ve got what your game needs."
        ),
        InfoPage(image = R.drawable.tournament2,
            description = "Step Into the Spotlight. Join local and national padel tournaments, track standings, and compete for glory, whether you're rising or reigning, your next big win starts here."
        )
    )

    val authViewModel: AuthViewModel = viewModel()
    val user = FirebaseAuth.getInstance().currentUser
    LaunchedEffect(user) {
        if (user != null) {
            navController.navigate(Routes.HOME) {
                authViewModel.saveUserToFirestore(user)
                popUpTo(Routes.ABOUT_APP) { inclusive = true }
            }
        }
    }

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

            AboutAppScroller(pages = pages, onFinished = { navController.navigate(Routes.AUTH) })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AboutAppScreenPreview() {
    AboutAppScreen(navController = rememberNavController())
}