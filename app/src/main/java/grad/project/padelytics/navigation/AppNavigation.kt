package grad.project.padelytics.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import grad.project.padelytics.features.auth.ui.AuthScreen
import grad.project.padelytics.features.auth.ui.LoginScreen
import grad.project.padelytics.features.auth.ui.SignUpScreen
import grad.project.padelytics.features.auth.ui.SignUpSecondScreen
import grad.project.padelytics.features.auth.viewModel.AuthViewModel
import grad.project.padelytics.features.favorite.ui.FavoriteScreen
import grad.project.padelytics.features.home.ui.HomeScreen
import grad.project.padelytics.features.profile.ui.ProfileScreen
import grad.project.padelytics.features.tournaments.ui.TournamentDetailsScreen
import grad.project.padelytics.features.tournaments.ui.TournamentsScreen

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
        composable(Routes.TOURNAMENT_DETAILS) {
            TournamentDetailsScreen(modifier,navController)
        }
    }
}