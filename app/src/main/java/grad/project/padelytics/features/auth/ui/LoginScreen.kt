package grad.project.padelytics.features.auth.ui

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import grad.project.padelytics.appComponents.MidWhiteHeadline
import grad.project.padelytics.features.auth.components.OutlinedTextFieldEmail
import grad.project.padelytics.features.auth.components.OutlinedTextFieldPasswordSignUp
import grad.project.padelytics.features.auth.components.WideGreenButton
import grad.project.padelytics.features.auth.viewModel.AuthViewModel
import grad.project.padelytics.navigation.Routes
import grad.project.padelytics.ui.theme.Blue

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier.fillMaxSize(),
    navController: NavHostController,
    viewModel: AuthViewModel = viewModel())
{

    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    BackHandler {
        navController.popBackStack()
    }

    Scaffold(Modifier.background(Blue).padding(25.dp)){
        innerPadding ->
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .background(Blue)
                    .padding(innerPadding),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start
            )
            {
                item{
                    MidWhiteHeadline("Login", 40)

                    Spacer(modifier = Modifier.height(20.dp))

                    OutlinedTextFieldEmail("Email", email) { email = it }

                    Spacer(modifier = Modifier.height(20.dp))

                    OutlinedTextFieldPasswordSignUp("Password", password) { password = it }

                }

                item {
                    WideGreenButton("Continue", {
                        viewModel.login(email, password) { success, errorMsg ->
                            if (success) {
                                navController.navigate(Routes.HOME)
                            } else {
                                Toast.makeText(context, "Login failed: $errorMsg", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    })
                    Spacer(modifier = Modifier.height(20.dp))
                }
        }
    }

}


@Preview
@Composable
fun LoginScreenPreview(){
    LoginScreen(navController = NavHostController(LocalContext.current))
}