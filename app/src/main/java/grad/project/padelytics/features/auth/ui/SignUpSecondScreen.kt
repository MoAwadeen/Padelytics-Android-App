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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import grad.project.padelytics.R
import grad.project.padelytics.appComponents.MidWhiteHeadline
import grad.project.padelytics.features.auth.components.CityDropdownMenu
import grad.project.padelytics.features.auth.components.DateInputField
import grad.project.padelytics.features.auth.components.IconLineRow
import grad.project.padelytics.features.auth.components.SingleSelectionButtonsGrid
import grad.project.padelytics.features.auth.components.WideGreenButton
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

    BackHandler(enabled = true) {
        // Block from back
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

                Spacer(modifier = Modifier.height(20.dp))
            }

            item {
                WideGreenButton(label = "Finish") {
                        val allFieldsSelected = listOf(selectedGender, selectedLevel, selectedCity, selectedDate)
                            .all { it != null }

                        if (allFieldsSelected) {
                            viewModel.addExtraFeature(
                                selectedGender!!,
                                selectedLevel!!,
                                selectedCity!!,
                                selectedDate!!
                            ) { success, errorMsg ->
                                if (success) {
                                    navController.navigate(Routes.HOME)
                                } else {
                                    Toast.makeText(
                                        context,
                                        errorMsg ?: "Something went wrong",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        } else {
                            Toast.makeText(context, "Please select all fields", Toast.LENGTH_SHORT).show()
                        }
                    }

            }
        }
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

@Preview(showSystemUi = true)
@Composable
fun SignUpSecondScreenPreview() {
    val dummyNavController = rememberNavController()
    val dummyViewModel = AuthViewModel(android.app.Application())
    SignUpSecondScreen(navController = dummyNavController, viewModel = dummyViewModel)
}
