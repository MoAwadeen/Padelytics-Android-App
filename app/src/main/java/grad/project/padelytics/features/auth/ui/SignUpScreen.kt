package grad.project.padelytics.features.auth.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsEndWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.annotations.concurrent.Background
import grad.project.padelytics.R
import grad.project.padelytics.appComponents.AppUtils
import grad.project.padelytics.appComponents.MidWhiteHeadline
import grad.project.padelytics.features.auth.components.GoogleSignInButton
import grad.project.padelytics.features.auth.components.WideBlueButton
import grad.project.padelytics.ui.theme.Blue
import grad.project.padelytics.ui.theme.WhiteGray
import grad.project.padelytics.ui.theme.lexendFontFamily
import grad.project.padelytics.features.auth.components.*
import grad.project.padelytics.features.auth.viewModel.AuthViewModel
import grad.project.padelytics.navigation.Routes

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier.fillMaxSize(),
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var photo by remember {  mutableStateOf("")  }
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Blue)
            .padding(25.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        MidWhiteHeadline("Sign Up", 40)

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextFieldName("First Name", firstName) { firstName = it }

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextFieldName("Last Name", lastName) { lastName = it }
        
        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextFieldName("Username", username) { username = it }

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextFieldEmail("Email", email) { email = it }

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextFieldPasswordSignUp("Password", password) { password = it }

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextFieldPasswordSignUp("Confirm Password", confirmPassword) { confirmPassword = it }

        Spacer(modifier = Modifier.height(100.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SmallBlueButton("Cancel") { navController.navigate(Routes.AUTH) }
            Spacer(modifier = Modifier.width(10.dp))
            SmallGreenButton("Continue") {
                if (password == confirmPassword) {
                    authViewModel.signup(email, password, firstName, lastName, username, photo) { success, message ->
                        if (success) {
                            navController.navigate(Routes.SECOND_SIGNUP)
                        } else {
                            AppUtils.showToast(context, message ?: "Something went wrong")
                        }
                    }
                } else {
                    AppUtils.showToast(context, "Passwords do not match")
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    SignUpScreen(navController = NavHostController(LocalContext.current), authViewModel = AuthViewModel(LocalContext.current.applicationContext as android.app.Application))
}
