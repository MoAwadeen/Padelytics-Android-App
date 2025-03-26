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
import grad.project.padelytics.features.home.ui.HomeScreen

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "auth") {
        composable("auth") {
            AuthScreen(modifier,navController)
        }
        composable("login") {
            LoginScreen(modifier,navController)
        }
        composable("signup") {
            SignUpScreen(navController = NavHostController(LocalContext.current), authViewModel = AuthViewModel(LocalContext.current.applicationContext as android.app.Application))
        }
        composable("signupSecond") {
            SignUpSecondScreen(navController = NavHostController(LocalContext.current), viewModel = AuthViewModel(LocalContext.current.applicationContext as android.app.Application))
        }
        composable("home") {
            HomeScreen(modifier,navController)
        }




    }
}