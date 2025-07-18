package grad.project.padelytics.navigation

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import grad.project.padelytics.features.about.ui.AboutAppScreen
import grad.project.padelytics.features.about.ui.AboutGameScreen
import grad.project.padelytics.features.about.ui.AboutUsScreen
import grad.project.padelytics.features.analysis.ui.AnalysisScreen
import grad.project.padelytics.features.auth.components.UsernameUpdateScreen
import grad.project.padelytics.features.auth.ui.AuthScreen
import grad.project.padelytics.features.auth.ui.LoginScreen
import grad.project.padelytics.features.auth.ui.SignUpScreen
import grad.project.padelytics.features.auth.ui.SignUpSecondScreen
import grad.project.padelytics.features.auth.viewModel.AuthViewModel
import grad.project.padelytics.features.courtBooking.ui.CourtDetailsScreen
import grad.project.padelytics.features.courtBooking.ui.CourtsScreen
import grad.project.padelytics.features.courtBooking.viewModel.CourtBookingViewModel
import grad.project.padelytics.features.favorites.ui.FavoritesScreen
import grad.project.padelytics.features.home.ui.HomeScreen
import grad.project.padelytics.features.profile.ui.ProfileScreen
import grad.project.padelytics.features.results.ui.ResultsScreen
import grad.project.padelytics.features.shop.ui.ProductDetailsScreen
import grad.project.padelytics.features.shop.ui.ShopScreen
import grad.project.padelytics.features.tournaments.ui.TournamentDetailsScreen
import grad.project.padelytics.features.tournaments.ui.TournamentsScreen
import grad.project.padelytics.features.videoUpload.ui.VideoUploadScreen

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val startDestination = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        val matchId = context.getSharedPreferences("match_prefs", Context.MODE_PRIVATE)
            .getString("match_id", null)

        Log.d("AppNavigation", "Launch match_id = $matchId")

        startDestination.value = if (!matchId.isNullOrEmpty()) Routes.ANALYSIS else Routes.ABOUT_APP
    }

    if (startDestination.value != null) {
        NavHost(navController = navController, startDestination = startDestination.value!!) {
            composable(Routes.AUTH) {
                AuthScreen(modifier,navController)
            }
            composable(Routes.LOGIN) {
                LoginScreen(modifier,navController)
            }
            composable(Routes.SIGNUP) {
                SignUpScreen(modifier,navController, authViewModel = AuthViewModel(LocalContext.current.applicationContext as android.app.Application))
            }
            composable(Routes.SECOND_SIGNUP) {
                SignUpSecondScreen(modifier,navController, viewModel = AuthViewModel(LocalContext.current.applicationContext as android.app.Application))
            }
            composable(Routes.HOME) {
                HomeScreen(modifier,navController)
            }
            composable(Routes.FAVORITE) {
                FavoritesScreen(modifier,navController)
            }
            composable(Routes.PROFILE) {
                ProfileScreen(modifier,navController)
            }
            composable(Routes.TOURNAMENTS) {
                TournamentsScreen(modifier,navController)
            }
            composable(
                route = Routes.TOURNAMENT_DETAILS,
                arguments = listOf(navArgument("tournamentId") { type = NavType.StringType })
            ) { backStackEntry ->
                val tournamentId = backStackEntry.arguments?.getString("tournamentId")
                TournamentDetailsScreen(modifier, navController, tournamentId)
            }
            composable(Routes.VIDEO_UPLOAD) {
                VideoUploadScreen(modifier,navController)
            }
            composable(Routes.USERNAME_UPDATE) {
                UsernameUpdateScreen(navController = navController)
            }
            composable(Routes.SHOP) {
                ShopScreen(modifier,navController)
            }
            composable(Routes.PRODUCT_DETAILS) {
                ProductDetailsScreen(modifier,navController)
            }
            composable(Routes.COURTS) {
                CourtsScreen(modifier,navController)
            }
            composable(
                route = Routes.COURT_DETAILS,
                arguments = listOf(navArgument("courtId") { type = NavType.StringType })
            ) { backStackEntry ->
                val courtId = backStackEntry.arguments?.getString("courtId")
                CourtDetailsScreen(modifier, navController, viewModel = CourtBookingViewModel(), courtId)
            }
            composable(Routes.RESULTS) {
                ResultsScreen(modifier,navController)
            }
            composable(Routes.ANALYSIS) {
                AnalysisScreen(modifier,navController)
            }
            composable(Routes.ABOUT_GAME) {
                AboutGameScreen(modifier,navController)
            }
            composable(Routes.ABOUT_US) {
                AboutUsScreen(modifier,navController)
            }
            composable(Routes.ABOUT_APP) {
                AboutAppScreen(modifier,navController)
            }
        }
    }
}