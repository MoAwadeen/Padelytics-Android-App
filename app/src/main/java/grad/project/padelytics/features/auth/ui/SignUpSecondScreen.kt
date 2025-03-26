package grad.project.padelytics.features.auth.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import grad.project.padelytics.R
import grad.project.padelytics.appComponents.MidWhiteHeadline
import grad.project.padelytics.features.auth.components.*
import grad.project.padelytics.features.auth.viewModel.AuthViewModel
import grad.project.padelytics.navigation.Routes
import grad.project.padelytics.ui.theme.Blue
import grad.project.padelytics.ui.theme.GreenLight
@Composable

fun SignUpSecondScreen(
    modifier: Modifier = Modifier.fillMaxSize(),
    navController: NavHostController,
    viewModel: AuthViewModel
) {
    val context = LocalContext.current
    var (selectedGender, setSelectedGender) = remember { mutableStateOf<String?>(null) }
    var (selectedLevel, setSelectedLevel) = remember { mutableStateOf<String?>(null) }

    val genderOptions = listOf("Male", "Female")
    val levelOptions = listOf("Beginner", "Intermediate", "Advanced", "Pro")

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

        // Gender Selection
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.gender),
                contentDescription = "Gender Icon",
                modifier = Modifier.size(25.dp),
                tint = GreenLight
            )
            Spacer(modifier = Modifier.width(12.dp))
            MidWhiteHeadline("What’s your gender ?", 24)
        }
        Spacer(modifier = Modifier.height(12.dp))
        SingleSelectionButtonsGrid(genderOptions) { selectedGender = it }

        Spacer(modifier = Modifier.height(32.dp))

        // Level Selection
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.chart),
                contentDescription = "Chart Icon",
                modifier = Modifier.size(25.dp),
                tint = GreenLight
            )
            Spacer(modifier = Modifier.width(12.dp))
            MidWhiteHeadline("What’s your level ?", 24)
        }
        Spacer(modifier = Modifier.height(12.dp))
        SingleSelectionButtonsGrid(levelOptions) { selectedLevel = it }

        Spacer(modifier = Modifier.height(200.dp))

        // Buttons Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SmallBlueButton("Back") { navController.navigate(Routes.SIGNUP) }
            Spacer(modifier = Modifier.width(10.dp))
            SmallGreenButton("Finish") {
                if (selectedGender != null && selectedLevel != null) {
                    viewModel.addExtraFeature(selectedGender!!, selectedLevel!!) { success, errorMsg ->
                        if (success) {
                            navController.navigate(Routes.HOME)
                        } else {
                            Toast.makeText(context, errorMsg ?: "Something went wrong", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(context, "Please select gender and level", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}


@Preview
@Composable
fun SignUpSecondScreenPreview() {
    SignUpSecondScreen(navController = NavHostController(LocalContext.current), viewModel = AuthViewModel(LocalContext.current.applicationContext as android.app.Application))
}
