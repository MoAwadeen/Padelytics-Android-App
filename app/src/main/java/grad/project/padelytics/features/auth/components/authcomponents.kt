package grad.project.padelytics.features.auth.components

import android.app.DatePickerDialog
import android.icu.util.Calendar
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import grad.project.padelytics.R
import grad.project.padelytics.appComponents.MidWhiteHeadline
import grad.project.padelytics.features.auth.viewModel.AuthViewModel
import grad.project.padelytics.navigation.Routes
import grad.project.padelytics.ui.theme.Blue
import grad.project.padelytics.ui.theme.BlueDark
import grad.project.padelytics.ui.theme.GreenDark
import grad.project.padelytics.ui.theme.GreenLight
import grad.project.padelytics.ui.theme.WhiteGray
import grad.project.padelytics.ui.theme.lexendFontFamily
import java.text.SimpleDateFormat
import java.util.Locale


@Composable
fun WideBlueButton(label: String, onClick: () -> Unit ){
    Button(onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(containerColor = BlueDark),
        modifier = Modifier.fillMaxWidth().height(60.dp),
        shape = RoundedCornerShape(100.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp))
    {
        Text(label,
            fontSize = 20.sp,
            color = Color.White ,
            fontFamily = lexendFontFamily,
            fontWeight = FontWeight.SemiBold)
    }
}


@Preview
@Composable
fun WideBlueButtonPreview(){
   WideBlueButton(label = "Login", onClick = {})
}


@Composable
fun SmallBlueButton(label: String, onClick: () -> Unit ){
    Button(onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(containerColor = BlueDark),
        modifier = Modifier.width(140.dp).height(60.dp),
        shape = RoundedCornerShape(100.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp))
    {
        Text(label,
            fontSize = 16.sp,
            color = Color.White ,
            fontFamily = lexendFontFamily,
            fontWeight = FontWeight.SemiBold)
    }
}


@Preview
@Composable
fun SmallBlueButtonPreview(){
    SmallBlueButton(label = "Login", onClick = {})
}


@Composable
fun SmallGreenButton(label: String, onClick: () -> Unit ){
    Button(onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(containerColor = GreenLight),
        modifier = Modifier.width(140.dp).height(60.dp),
        shape = RoundedCornerShape(100.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp))
    {
        Text(label,
            fontSize = 16.sp,
            color = BlueDark ,
            fontFamily = lexendFontFamily,
            fontWeight = FontWeight.SemiBold)
    }
}


@Composable
fun WideGreenButton(label: String, onClick: () -> Unit ){
    Button(onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(containerColor = GreenLight),
        modifier = Modifier.fillMaxWidth().height(60.dp),
        shape = RoundedCornerShape(100.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp))
    {
        Text(label,
            fontSize = 20.sp,
            color = BlueDark ,
            fontFamily = lexendFontFamily,
            fontWeight = FontWeight.SemiBold)
    }
}

@Preview
@Composable
fun SmallGreenButtonPreview(){
    SmallGreenButton(label = "Login", onClick = {})
}



@Composable
fun GoogleSignInButton(onClick: () -> Unit) {

    Button(
        colors = ButtonDefaults.buttonColors(containerColor = Blue),
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        border = BorderStroke(5.dp, BlueDark),
        shape = RoundedCornerShape(100.dp),
        elevation = ButtonDefaults.buttonElevation(0.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth(),

            ) {
            Image(
                painter = painterResource(id = R.drawable.google), // Add Google logo in drawable
                contentDescription = "Google Sign-In",
                modifier = Modifier
                    .size(28.dp)
            )
            Spacer(modifier = Modifier.width(18.dp))
            Text(
                text = "Login with Google",
                color = White,
                fontSize = 20.sp,
                fontFamily = lexendFontFamily,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1
            )
        }
    }
}

@Preview
@Composable
fun GoogleSignInButtonPreview() {
    GoogleSignInButton(onClick = {})
}

@Composable
fun OutlinedTextFieldName(
    label: String,
    userinput: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
) {
    val isError = userinput.isNotEmpty() && userinput.trim().length < 2

    OutlinedTextField(
        modifier = modifier,
        value = userinput,
        onValueChange = onValueChange,
        label = { Text(label, fontFamily = lexendFontFamily) },
        singleLine = true,
        isError = isError,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = if (isError) Color.Red else GreenLight,
            unfocusedBorderColor = if (isError) Color.Red else BlueDark,
            cursorColor = GreenLight,
            focusedLabelColor = GreenLight,
            unfocusedLabelColor = GreenLight,
            errorBorderColor = Color.Red,
            errorCursorColor = Color.Red,
            errorLabelColor = Color.Red,
            disabledTextColor = WhiteGray,
            focusedTextColor = WhiteGray,
            unfocusedTextColor = WhiteGray,
            errorTextColor = Color.Red
        )
    )
}

@Preview
@Composable
fun OutlinedTextFieldNamePreview(){
    OutlinedTextFieldName("First Name", "", modifier = Modifier.fillMaxWidth(),{})}

@Composable
fun OutlinedTextFieldEmail(
    label: String,
    userInput: String,
    onValueChange: (String) -> Unit
) {
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+.[A-Za-z0-9.-]+$".toRegex()
    val isError = userInput.isNotEmpty() && !userInput.matches(emailRegex)

    OutlinedTextField(
        value = userInput,
        onValueChange = onValueChange,
        label = { Text(label, fontFamily = lexendFontFamily) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        isError = isError,
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = if (isError) Color.Red else GreenLight,
            unfocusedBorderColor = if (isError) Color.Red else BlueDark,
            cursorColor = GreenLight,
            focusedLabelColor = GreenLight,
            unfocusedLabelColor = GreenLight,
            errorBorderColor = Color.Red,
            errorCursorColor = Color.Red,
            errorLabelColor = Color.Red,
            disabledTextColor = WhiteGray,
            focusedTextColor = WhiteGray,
            unfocusedTextColor = WhiteGray,
            errorTextColor = Color.Red
        )
    )
}

@Preview
@Composable
fun OutlinedTextFieldEmailPreview(){
    OutlinedTextFieldEmail("Email","",{})}
@Composable
fun OutlinedTextFieldPasswordSignUp(
    label: String,
    password: String,
    onValueChange: (String) -> Unit
) {
    var passwordVisible by remember { mutableStateOf(false) }

    val passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$".toRegex()
    val isError = password.isNotEmpty() && !password.matches(passwordRegex)

    OutlinedTextField(
        value = password,
        onValueChange = onValueChange,
        label = { Text(label, fontFamily = lexendFontFamily) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        isError = isError,
        trailingIcon = {
            val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = image, contentDescription = "Toggle Password Visibility", tint = GreenLight)
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = if (isError) Color.Red else GreenLight,
            unfocusedBorderColor = if (isError) Color.Red else BlueDark,
            cursorColor = GreenLight,
            focusedLabelColor = GreenLight,
            unfocusedLabelColor = GreenLight,
            errorBorderColor = Color.Red,
            errorCursorColor = Color.Red,
            errorLabelColor = Color.Red,
            disabledTextColor = WhiteGray,
            focusedTextColor = WhiteGray,
            unfocusedTextColor = WhiteGray,
            errorTextColor = Color.Red
        )
    )
}

@Preview
@Composable
fun OutlinedTextFieldPasswordSignUpPreview(){
    OutlinedTextFieldPasswordSignUp("Password","",{})}

@Composable
fun SingleSelectionButtonsGrid(options: List<String>, onSelectionChange: (String) -> Unit) {
    var selectedButton by remember { mutableStateOf<Int?>(null) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        options.chunked(2).forEach { rowButtons ->
            Row {
                rowButtons.forEach { text ->
                    val buttonIndex = options.indexOf(text)
                    Button(
                        onClick = {
                            selectedButton = buttonIndex
                            onSelectionChange(text) // Notify parent about the selected option
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedButton == buttonIndex) GreenLight else Blue
                        ),
                        border = BorderStroke(5.dp, if (selectedButton == buttonIndex) GreenLight else BlueDark),
                        shape = RoundedCornerShape(100.dp),
                        elevation = ButtonDefaults.buttonElevation(0.dp),
                        modifier = Modifier
                            .padding(8.dp)
                            .weight(1f)
                            .width(140.dp)
                            .height(48.dp)
                    ) {
                        Text(
                            text = text,
                            fontSize = 12.sp,
                            color = if (selectedButton == buttonIndex) BlueDark else WhiteGray,
                            fontFamily = lexendFontFamily,
                            fontWeight = if (selectedButton == buttonIndex) FontWeight.Bold else FontWeight.Normal,
                            maxLines = 1,
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSingleSelectionButtonsGrid() {
    val buttons = listOf("Option 1", "Option 2", "Option 3", "Option 4")
    SingleSelectionButtonsGrid(buttons) {}
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityDropdownMenu(
    selectedCity: String,
    onValueChange: (String) -> Unit,
    isError: Boolean = false
) {
    val cities = listOf("Cairo", "Giza", "Alexandria", "Dakahlia","Beheira", "Matrouh", "Kafr El Sheikh", "Gharbia", "Menofia",
        "Damietta", "Port Said", "Ismailia", "Suez", "Sharqia", "Qalyubia", "Fayoum", "Beni Suef", "Minya", "Assiut", "Sohag",
        "Qena", "Luxor", "Aswan", "Red Sea", "New Valley", "North Sinai", "South Sinai")

    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedCity,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            label = { Text("Select a City", fontFamily = lexendFontFamily) },
            singleLine = true,
            isError = isError,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (isError) Color.Red else GreenLight,
                unfocusedBorderColor = if (isError) Color.Red else BlueDark,
                cursorColor = GreenLight,
                focusedLabelColor = GreenLight,
                unfocusedLabelColor = GreenLight,
                errorBorderColor = Color.Red,
                errorCursorColor = Color.Red,
                errorLabelColor = Color.Red,
                disabledTextColor = WhiteGray,
                focusedTextColor = WhiteGray,
                unfocusedTextColor = WhiteGray,
            ),
            trailingIcon = {
                Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown Icon", tint = GreenLight)
            }
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            cities.forEach { city ->
                DropdownMenuItem(
                    text = { Text(city, fontFamily = lexendFontFamily) },
                    onClick = {
                        onValueChange(city) // Notify parent of the change
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun CityDropdownMenuPreview() {
    var selectedCity by remember { mutableStateOf("Cairo") }
    CityDropdownMenu( selectedCity = selectedCity,
        onValueChange = { newCity ->
            selectedCity = newCity // Update the state when selection changes
        })
}


@Composable
fun DateInputField(
    selectedDate: String,          
    onValueChange: (String) -> Unit,
    isError: Boolean = false
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val newDate = Calendar.getInstance()
            newDate.set(year, month, dayOfMonth)
            onValueChange(dateFormatter.format(newDate.time))
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    OutlinedTextField(
        value = selectedDate,
        onValueChange = {},
        readOnly = true,
        modifier = Modifier.fillMaxWidth(),
        label = { Text("Select Date", fontFamily = lexendFontFamily, color = GreenLight) },
        singleLine = true,
        isError = isError,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = if (isError) Color.Red else GreenLight,
            unfocusedBorderColor = if (isError) Color.Red else BlueDark,
            cursorColor = GreenLight,
            focusedLabelColor = GreenLight,
            unfocusedLabelColor = GreenLight,
            errorBorderColor = Color.Red,
            errorCursorColor = Color.Red,
            errorLabelColor = Color.Red,
            disabledTextColor = GreenLight,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            disabledLabelColor = GreenLight,
            disabledPlaceholderColor = GreenLight,
            errorTrailingIconColor = Color.Red
        ),
        trailingIcon = {
            IconButton(onClick = { datePickerDialog.show() }) {
                Icon(Icons.Default.CalendarToday, contentDescription = "Calendar Icon", tint = GreenLight)
            }
        }
    )
}


@Preview
@Composable
fun DateInputFieldPreview() {
    var selectedDate by remember { mutableStateOf("Select Date") }
    DateInputField(selectedDate = selectedDate,
        onValueChange = { newDate ->
            selectedDate = newDate
        })
}



@Composable
internal fun IconLineRow(iconId: Int, title: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = GreenLight
        )
        Spacer(modifier = Modifier.width(12.dp))
        MidWhiteHeadline(title, 20)
    }
    Spacer(modifier = Modifier.height(5.dp))
}

@Preview
@Composable
fun IconLineRowPreview() {
    IconLineRow(iconId = R.drawable.gender, title = "What’s your gender?")
}

@Composable
fun UpdateUsernameDialog(
    onDismiss: () -> Unit = {},
    viewModel: AuthViewModel = viewModel(),
    navController: NavHostController
) {
    val context = LocalContext.current
    var username by remember { mutableStateOf("") }
    var isUpdating by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        containerColor = Blue,
        titleContentColor = BlueDark,
        textContentColor = GreenLight,
        onDismissRequest = {
            if (!isUpdating) {
                errorMessage = null
                onDismiss()
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    isUpdating = true
                    viewModel.updateUsername(username) { success, error ->
                        isUpdating = false
                        if (success) {
                            navController.navigate(Routes.SECOND_SIGNUP) {
                                popUpTo("auth") { inclusive = true }
                            }
                        } else {
                            errorMessage = error ?: "Update failed"
                        }
                    }
                },
                enabled = username.isNotBlank() && !isUpdating,
                colors = ButtonDefaults.buttonColors(
                    containerColor = GreenLight,
                    contentColor = BlueDark
                )
            ) {
                Text(
                    text = if (isUpdating) "UPDATING..." else "CONFIRM",
                    fontFamily = lexendFontFamily,
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    if (!isUpdating) {
                        errorMessage = null
                        onDismiss()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = GreenDark,
                    contentColor = GreenLight
                )
            ) {
                Text(
                    text = "CANCEL",
                    fontFamily = lexendFontFamily,
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        title = {
            Text(
                text = "Choose Username",
                fontFamily = lexendFontFamily,
                fontWeight = FontWeight.Bold,
                color = WhiteGray
            )
        },
        text = {
            Column {
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GreenLight,
                        unfocusedBorderColor = BlueDark,
                        cursorColor = GreenLight,
                        focusedLabelColor = GreenLight,
                        unfocusedLabelColor = GreenLight,
                        errorBorderColor = Color.Red,
                        errorCursorColor = Color.Red,
                        errorLabelColor = Color.Red,
                        disabledTextColor = WhiteGray,
                        focusedTextColor = WhiteGray,
                        unfocusedTextColor = WhiteGray,
                        errorTextColor = GreenDark
                    )
                )

                if (isUpdating) {
                    LinearProgressIndicator(
                        modifier = Modifier.padding(16.dp),
                        color = GreenLight,
                        trackColor = BlueDark
                    )
                }

                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = GreenDark,
                        modifier = Modifier.padding(top = 16.dp),
                        fontFamily = lexendFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    )
}

@Composable
fun UsernameUpdateScreen(
    navController: NavHostController,
    viewModel: AuthViewModel = viewModel()
) {
    var showDialog by remember { mutableStateOf(true) }

    BackHandler {
        //Block the back button
    }

    if (showDialog) {
        UpdateUsernameDialog(
            onDismiss = { showDialog = false },
            viewModel = viewModel,
            navController = navController
        )
    } else {
        LaunchedEffect(Unit) {
            navController.navigate(Routes.AUTH) {
                popUpTo(Routes.AUTH) { inclusive = true }
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize().background(Blue))
}


@Preview
@Composable
fun UpdateUsernameDialogPreview() {
    UpdateUsernameDialog( onDismiss = {}, navController = NavHostController(LocalContext.current))
}

