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
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import grad.project.padelytics.R
import grad.project.padelytics.appComponents.MidWhiteHeadline
import grad.project.padelytics.features.auth.components.*
import grad.project.padelytics.features.auth.viewModel.AuthViewModel
import grad.project.padelytics.navigation.Routes
import grad.project.padelytics.ui.theme.Blue

@Composable
fun SignUpSecondScreen(
    modifier: Modifier = Modifier.fillMaxSize(),
    navController: NavHostController,
    viewModel: AuthViewModel
) {
    val context = LocalContext.current
    var selectedGender by remember { mutableStateOf<String?>(null) }
    var selectedLevel by remember { mutableStateOf<String?>(null) }
    var selectedCity by remember { mutableStateOf<String?>(null) }
    var selectedDate by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .background(Blue)
            .padding(25.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        MidWhiteHeadline("Sign Up", 30)
        Spacer(modifier = Modifier.height(20.dp))

        UserInputSection(
            title = "What’s your gender?",
            iconId = R.drawable.gender,
            options = listOf("Male", "Female"),
            selectedValue = selectedGender,
            onSelectionChange = { selectedGender = it }
        )

        Spacer(modifier = Modifier.height(20.dp))

        UserInputSection(
            title = "What’s your level?",
            iconId = R.drawable.chart,
            options = listOf("Beginner", "Intermediate", "Advanced", "Pro"),
            selectedValue = selectedLevel,
            onSelectionChange = { selectedLevel = it }
        )

        Spacer(modifier = Modifier.height(20.dp))

        IconLineRow(iconId = R.drawable.location_bold, title = "Where are you from?")
        CityDropdownMenu(
            selectedCity = selectedCity ?: "",
            onValueChange = { selectedCity = it }
        )

        Spacer(modifier = Modifier.height(20.dp))

        IconLineRow(iconId = R.drawable.calendar, title = "When is your birthday?")
        DateInputField(
            selectedDate = selectedDate ?: "",
            onValueChange = { selectedDate = it }
        )

        Spacer(modifier = Modifier.height(48.dp))
        NavigationButtons(navController, selectedGender, selectedLevel, selectedCity, selectedDate, viewModel, context)
    }
}

@Composable
private fun UserInputSection(
    title: String,
    iconId: Int,
    options: List<String>,
    selectedValue: String?,
    onSelectionChange: (String) -> Unit
) {
    IconLineRow(iconId = iconId, title = title)
    SingleSelectionButtonsGrid(options, onSelectionChange)
}

@Composable
private fun NavigationButtons(
    navController: NavHostController,
    selectedGender: String?,
    selectedLevel: String?,
    selectedCity: String?,
    selectedDate: String?,
    viewModel: AuthViewModel,
    context: android.content.Context
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SmallBlueButton("Back") { navController.popBackStack() }

        Spacer(modifier = Modifier.width(10.dp))

        SmallGreenButton("Finish") {
            val allFieldsSelected = listOf(selectedGender, selectedLevel, selectedCity, selectedDate).all { it != null }
            if (allFieldsSelected) {
                viewModel.addExtraFeature(selectedGender!!, selectedLevel!!, selectedCity!!, selectedDate!!) { success, errorMsg ->
                    if (success) {
                        navController.navigate(Routes.HOME)
                    } else {
                        Toast.makeText(context, errorMsg ?: "Something went wrong", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(context, "Please select all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@Preview()
@Composable
fun SignUpSecondScreenPreview() {
    val dummyNavController = NavHostController(LocalContext.current)
    val dummyViewModel = AuthViewModel(android.app.Application())
    SignUpSecondScreen(navController = dummyNavController, viewModel = dummyViewModel)
}
