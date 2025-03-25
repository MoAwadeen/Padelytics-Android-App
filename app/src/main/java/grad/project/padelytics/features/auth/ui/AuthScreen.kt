package grad.project.padelytics.features.auth.ui

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import grad.project.padelytics.R
import grad.project.padelytics.features.auth.components.GoogleSignInButton
import grad.project.padelytics.features.auth.components.WideBlueButton
import grad.project.padelytics.ui.theme.Blue
import grad.project.padelytics.ui.theme.WhiteGray
import grad.project.padelytics.ui.theme.lexendFontFamily

@Composable
fun AuthScreen(modifier: Modifier = Modifier.fillMaxSize(), navController: NavHostController) {
        Column (
            modifier = modifier
                .fillMaxSize()
                .background(Blue),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
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

                WideBlueButton(label = "I have an account", onClick = {navController.navigate("login")})

                Spacer(modifier = Modifier.height(16.dp))

                WideBlueButton(label = "Sign Up", onClick = {navController.navigate("signup")})

                Spacer(modifier = Modifier.height(16.dp))

                Text(modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = "or",
                    color = WhiteGray,
                    fontSize = 20.sp,
                    fontFamily = lexendFontFamily,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Medium)

                Spacer(modifier = Modifier.height(16.dp))

                GoogleSignInButton(onClick = {})

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
fun AuthScreenPreview(){
    AuthScreen(navController = NavHostController(LocalContext.current))
}

