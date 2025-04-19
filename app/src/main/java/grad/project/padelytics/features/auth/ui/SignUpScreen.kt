package grad.project.padelytics.features.auth.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import grad.project.padelytics.appComponents.AppUtils
import grad.project.padelytics.appComponents.MidWhiteHeadline
import grad.project.padelytics.features.auth.components.*
import grad.project.padelytics.features.auth.viewModel.AuthViewModel
import grad.project.padelytics.navigation.Routes
import grad.project.padelytics.ui.theme.Blue

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier.fillMaxSize(),
    navController: NavHostController,
    authViewModel: AuthViewModel = viewModel()
) {
    val context = LocalContext.current

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var photo by remember { mutableStateOf("") }

    BackHandler {
        navController.popBackStack()
    }

    Scaffold(
        modifier = Modifier.background(Blue).padding(25.dp)
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(Blue)
                .padding(innerPadding),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            item {
                MidWhiteHeadline("Sign Up", 40)

                Spacer(modifier = Modifier.height(20.dp))

                Row( modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically)
                {
                    OutlinedTextFieldName("First Name", firstName, modifier = Modifier.weight(1f)) { firstName = it }

                    Spacer(modifier = Modifier.width(10.dp))

                    OutlinedTextFieldName("Last Name", lastName, modifier = Modifier.weight(1f)) { lastName = it }
                }

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextFieldName("Username", username, modifier = Modifier.fillMaxWidth()) { username = it }

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextFieldEmail("Email", email) { email = it }

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextFieldPasswordSignUp("Password", password) { password = it }

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextFieldPasswordSignUp("Confirm Password", confirmPassword) { confirmPassword = it }

                Spacer(modifier = Modifier.height(20.dp))
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SmallBlueButton("Cancel") {
                        navController.navigate(Routes.AUTH)
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    SmallGreenButton("Continue") {
                        if (password != confirmPassword) {
                            AppUtils.showToast(context, "Passwords do not match")
                            return@SmallGreenButton
                        }

                        if (username.isBlank()) {
                            AppUtils.showToast(context, "Username cannot be empty")
                            return@SmallGreenButton
                        }

                        authViewModel.checkUsernameAvailable(username) { isAvailable, errorMsg ->
                            if (isAvailable) {
                                authViewModel.signup(
                                    email,
                                    password,
                                    firstName,
                                    lastName,
                                    username,
                                    photo
                                ) { success, message ->
                                    if (success) {
                                        navController.navigate(Routes.SECOND_SIGNUP)
                                    } else {
                                        AppUtils.showToast(
                                            context,
                                            message ?: "Something went wrong"
                                        )
                                    }
                                }
                            } else {
                                AppUtils.showToast(context, errorMsg ?: "Username is already taken")
                            }
                        }
                    }

                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun SignUpScreenPreview() {
    val navController = rememberNavController()
    SignUpScreen(navController = navController)
}
