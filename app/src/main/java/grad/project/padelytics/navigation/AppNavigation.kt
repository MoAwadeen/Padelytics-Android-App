package grad.project.padelytics.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import grad.project.padelytics.features.auth.components.UsernameUpdateScreen
import grad.project.padelytics.features.auth.ui.AuthScreen
import grad.project.padelytics.features.auth.ui.LoginScreen
import grad.project.padelytics.features.auth.ui.SignUpScreen
import grad.project.padelytics.features.auth.ui.SignUpSecondScreen
import grad.project.padelytics.features.auth.viewModel.AuthViewModel
import grad.project.padelytics.features.courtBooking.ui.CourtDetailsScreen
import grad.project.padelytics.features.courtBooking.ui.CourtsScreen
import grad.project.padelytics.features.favorite.ui.FavoriteScreen
import grad.project.padelytics.features.home.ui.HomeScreen
import grad.project.padelytics.features.profile.ui.ProfileScreen
import grad.project.padelytics.features.shop.ui.ProductDetailsScreen
import grad.project.padelytics.features.shop.ui.ShopScreen
import grad.project.padelytics.features.tournaments.ui.TournamentDetailsScreen
import grad.project.padelytics.features.tournaments.ui.TournamentsScreen
import grad.project.padelytics.features.videoUpload.ui.VideoUploadScreen

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.AUTH) {
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
            FavoriteScreen(modifier,navController)
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
        composable(Routes.COURT_DETAILS) {
            CourtDetailsScreen(modifier,navController)
        }
    }
}