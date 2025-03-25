package grad.project.padelytics.features.auth.components

import android.app.DatePickerDialog
import android.icu.util.Calendar
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults.outlinedTextFieldColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import grad.project.padelytics.ui.theme.Blue
import grad.project.padelytics.ui.theme.BlueDark
import grad.project.padelytics.ui.theme.lexendFontFamily
import grad.project.padelytics.R
import grad.project.padelytics.appComponents.MidWhiteHeadline
import grad.project.padelytics.ui.theme.GreenLight
import grad.project.padelytics.ui.theme.WhiteGray
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.platform.LocalContext


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
    onValueChange: (String) -> Unit
) {
    val isError = userinput.isNotEmpty() && userinput.trim().length < 2

    OutlinedTextField(
        value = userinput,
        onValueChange = onValueChange,
        label = { Text(label, fontFamily = lexendFontFamily) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        isError = isError,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = if (isError) Color.Red else GreenLight,
            unfocusedBorderColor = if (isError) Color.Red else BlueDark,
            cursorColor = GreenLight,
            focusedLabelColor = GreenLight,
            unfocusedLabelColor = GreenLight,
            errorBorderColor = Color.Red,
            errorCursorColor = Color.Red,
            errorLabelColor = Color.Red
        )
    )
}

@Preview
@Composable
fun OutlinedTextFieldNamePreview(){
    OutlinedTextFieldName("First Name","",{})}
@Composable
fun OutlinedTextFieldPasswordSignUp(
    label: String,
    password: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = password,
        onValueChange = onValueChange,
        label = { Text(label, fontFamily = lexendFontFamily) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = GreenLight,
            unfocusedBorderColor = BlueDark,
            cursorColor = GreenLight,
            focusedLabelColor = GreenLight,
            unfocusedLabelColor = GreenLight
        )
    )
}


@Preview
@Composable
fun OutlinedTextFieldPasswordSignUpPreview(){
    val password by remember { mutableStateOf("") }
    OutlinedTextFieldPasswordSignUp(label = "Password", password = password, onValueChange = {})}

@Composable
fun OutlinedTextFieldConfirmPassword(
    password: String,
    confirmPassword: String,
) {
    var inputvalue by remember { mutableStateOf(confirmPassword) }
    val localFocusManager = LocalFocusManager.current
    val isError = confirmPassword.isNotEmpty() && confirmPassword != password

    Column {
        Box(modifier = Modifier.background(Blue)) {
            OutlinedTextField(
                value = inputvalue,
                onValueChange = {inputvalue = it},
                label = { Text("Confirm Password", fontFamily = lexendFontFamily) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                isError = isError,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions {
                    localFocusManager.clearFocus()
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (isError) Color.Red else GreenLight,
                    unfocusedBorderColor = if (isError) Color.Red else BlueDark,
                    cursorColor = GreenLight,
                    focusedLabelColor = GreenLight,
                    unfocusedLabelColor = GreenLight,
                    errorBorderColor = Color.Red,
                    errorCursorColor = Color.Red,
                    errorLabelColor = Color.Red
                )
            )
        }

        if (isError) {
            Text(
                text = "Passwords do not match",
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

@Preview
@Composable
fun OutlinedTextFieldConfirmPasswordPreview() {
    val password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    OutlinedTextFieldConfirmPassword(
        password = password,
        confirmPassword = confirmPassword)
}

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


@Composable
fun AgeDatePicker() {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    var selectedDate by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }

    val datePickerDialog = DatePickerDialog(
        context,
        { _, selectedYear, selectedMonth, selectedDay ->
            val birthCalendar = Calendar.getInstance().apply {
                set(selectedYear, selectedMonth, selectedDay)
            }
            selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"

            // Calculate Age
            val currentYear = Calendar.getInstance().get(Calendar.YEAR)
            val calculatedAge = currentYear - selectedYear
            age = "$calculatedAge years old"
        },
        year, month, day
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Selected Date: $selectedDate", fontSize = 18.sp)
        Text(text = "Age: $age", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Blue)

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { datePickerDialog.show() }) {
            Text(text = "Select Birth Date")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAgeDatePicker() {
    AgeDatePicker()
}


