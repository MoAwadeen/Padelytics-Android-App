package grad.project.padelytics.features.home.ui

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
import grad.project.padelytics.appComponents.MidWhiteHeadline
import grad.project.padelytics.features.auth.components.GoogleSignInButton
import grad.project.padelytics.features.auth.components.OutlinedTextFieldConfirmPassword
import grad.project.padelytics.features.auth.components.OutlinedTextFieldName
import grad.project.padelytics.features.auth.components.OutlinedTextFieldPasswordSignUp
import grad.project.padelytics.features.auth.components.SmallBlueButton
import grad.project.padelytics.features.auth.components.SmallGreenButton
import grad.project.padelytics.features.auth.components.WideBlueButton
import grad.project.padelytics.features.auth.components.WideGreenButton
import grad.project.padelytics.ui.theme.Blue
import grad.project.padelytics.ui.theme.WhiteGray
import grad.project.padelytics.ui.theme.lexendFontFamily

@Composable
fun HomeScreen(modifier: Modifier = Modifier.fillMaxSize().background(WhiteGray),navController: NavHostController) {

    Box(modifier = Modifier.fillMaxSize().background(WhiteGray)) {
        Spacer(modifier = Modifier.height(100.dp))
        WideGreenButton("Log out", {navController.navigate("auth")})
    }
}


@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(navController = NavHostController(LocalContext.current))
}