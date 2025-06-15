package grad.project.padelytics.features.auth.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import grad.project.padelytics.R
import grad.project.padelytics.features.auth.components.GoogleSignInButton
import grad.project.padelytics.features.auth.components.WideBlueButton
import grad.project.padelytics.features.auth.viewModel.AuthViewModel
import grad.project.padelytics.navigation.Routes
import grad.project.padelytics.ui.theme.Blue
import grad.project.padelytics.ui.theme.WhiteGray
import grad.project.padelytics.ui.theme.lexendFontFamily

@SuppressLint("ContextCastToActivity")
@Composable
fun AuthScreen(
    modifier: Modifier = Modifier.fillMaxSize(),
    navController: NavHostController,
    viewModel: AuthViewModel = viewModel()
) {

    val user = FirebaseAuth.getInstance().currentUser
    LaunchedEffect(user) {
        if (user != null) {
            navController.navigate(Routes.HOME) {
                viewModel.saveUserToFirestore(user)
                popUpTo("auth") { inclusive = true }
            }
        }
    }
    val context = LocalContext.current

    val activity = (LocalContext.current as? Activity)
    BackHandler(enabled = true) {
        activity?.finish()
    }

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            val account = task.getResult(ApiException::class.java)
            account?.idToken?.let { idToken ->
                viewModel.handleGoogleSignInToken(idToken) { success, isExistingUser, errorMsg ->
                    if (success) {
                        if (isExistingUser) {
                            navController.navigate(Routes.HOME) {
                                popUpTo("auth") { inclusive = true }
                            }
                        } else {
                            navController.navigate(Routes.USERNAME_UPDATE) {
                                popUpTo("auth") { inclusive = true }
                            }
                        }
                    } else {
                        Toast.makeText(
                            context,
                            errorMsg ?: "Google Sign-In failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            } ?: run {
                Toast.makeText(context, "Google Sign-In failed: No ID Token", Toast.LENGTH_SHORT).show()
            }
        } catch (e: ApiException) {
            Log.e("AuthScreen", "Google Sign-In error", e)
            Toast.makeText(context, "Google Sign-In failed", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Blue),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(120.dp))
        Image(
            painter = painterResource(id = R.drawable.padelytics_),
            contentDescription = "Logo",
            modifier = Modifier.width(250.dp).height(80.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.authsplash),
            contentDescription = "design",
            modifier = Modifier.fillMaxWidth()
        )

        Column(modifier = Modifier.padding(40.dp)) {
            WideBlueButton(
                label = "I have an account",
                onClick = { navController.navigate(Routes.LOGIN) }
            )
            Spacer(modifier = Modifier.height(16.dp))
            WideBlueButton(
                label = "Sign Up",
                onClick = { navController.navigate(Routes.SIGNUP) }
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = "or",
                color = WhiteGray,
                fontSize = 20.sp,
                fontFamily = lexendFontFamily
            )

            Spacer(modifier = Modifier.height(16.dp))

            GoogleSignInButton(
                onClick = {
                    viewModel.googleSignIn(googleSignInLauncher) { success, errorMsg ->
                        if (!success) {
                            errorMsg?.let {
                                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            )
        }

        Image(
            painter = painterResource(id = R.drawable.authsplashend),
            contentDescription = "end",
            modifier = Modifier.fillMaxHeight().fillMaxWidth(),
            alignment = Alignment.BottomCenter,
        )
    }
}

@Preview
@Composable
fun AuthScreenPreview() {
    AuthScreen(navController = NavHostController(LocalContext.current))
}