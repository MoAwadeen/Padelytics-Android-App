package grad.project.padelytics.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
            SignUpScreen(modifier,navController,AuthViewModel())
        }
        composable("signupSecond") {
            SignUpSecondScreen(modifier,navController,AuthViewModel())
        }
        composable("home") {
            HomeScreen(modifier,navController)
        }




    }
}