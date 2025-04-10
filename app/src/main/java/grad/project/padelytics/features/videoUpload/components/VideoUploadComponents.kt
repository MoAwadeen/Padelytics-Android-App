package grad.project.padelytics.features.videoUpload.components

import android.graphics.Color.TRANSPARENT
import androidx.activity.ComponentActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import grad.project.padelytics.R
import grad.project.padelytics.appComponents.MidDarkHeadline
import grad.project.padelytics.features.videoUpload.data.FriendPlayer
import grad.project.padelytics.features.videoUpload.viewModel.FavouriteTournamentsViewModel
import grad.project.padelytics.ui.theme.Blue
import grad.project.padelytics.ui.theme.BlueDark
import grad.project.padelytics.ui.theme.GreenLight
import grad.project.padelytics.ui.theme.WhiteGray
import grad.project.padelytics.ui.theme.lexendFontFamily

@Composable
fun VideoUploadCard(uploadOnClick: () -> Unit) {
    Image(
        painter = painterResource(id = R.drawable.upload_video),
        contentDescription = "upload icon",
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .clickable { uploadOnClick() },
        contentScale = ContentScale.Fit,
        alignment = Alignment.Center
    )
}

@Preview(device = "id:pixel_4")
@Composable
fun VideoUploadPreview() {
    VideoUploadCard(){}
}




@Composable
fun OldPlayersGrid(options: List<String>, onSelectionChange: (String) -> Unit) {
    var FilledButton by remember { mutableStateOf<Int?>(null) }

    MidDarkHeadline(text = "Players", size = 24)
    Spacer(modifier = Modifier.height(4.dp))
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        options.chunked(2).forEach { rowButtons ->
            Row(modifier = Modifier.padding(vertical = 4.dp)) {
                rowButtons.forEach { text ->
                    val buttonIndex = options.indexOf(text)
                    Button(
                        onClick = {
                            FilledButton = buttonIndex
                            onSelectionChange(text) // Notify parent about the selected option
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (FilledButton == buttonIndex) GreenLight else WhiteGray
                        ),
                        border = BorderStroke(5.dp, if (FilledButton == buttonIndex) GreenLight else GreenLight),
                        shape = RoundedCornerShape(100.dp),
                        elevation = ButtonDefaults.buttonElevation(0.dp),
                        modifier = Modifier
                            .weight(1f)
                            .width(140.dp)
                            .height(74.dp)
                            .padding(2.dp)
                    ) {
                       Row(
                           verticalAlignment = Alignment.CenterVertically,
                           horizontalArrangement = Arrangement.Center,
                           modifier = Modifier.fillMaxWidth(),
                       ) {
                           Image(
                           painter = painterResource(id = R.drawable.user),
                           contentDescription = "Avatar",
                           contentScale = ContentScale.Fit,
                           modifier = Modifier
                               .size(40.dp)
                               .border(2.dp, BlueDark, CircleShape)
                               .clip(CircleShape)
                               .background(Transparent)
                       )
                           Spacer(modifier = Modifier.width(8.dp))
                           Text(
                               text = text,
                               fontSize = 20.sp,
                               color = if (FilledButton == buttonIndex) BlueDark else BlueDark,
                               fontFamily = lexendFontFamily,
                               fontWeight = if (FilledButton == buttonIndex) FontWeight.Bold else FontWeight.SemiBold,
                               maxLines = 1,
                           ) }

                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun OldPlayersGridPreview() {
    val options = listOf("Player 1", "Player 2", "Player 3", "Player 4")
    OldPlayersGrid(options = options, onSelectionChange = {})
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourtDropdownMenu(
    selectedCourt: String = "",
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
) {

    val localViewModel: FavouriteTournamentsViewModel = viewModel()
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    val tournamentNames by localViewModel.tournamentNames.collectAsState()

    LaunchedEffect(key1 = true) {
        userId?.let {
            if (localViewModel.tournamentNames.value.isEmpty()) {
                localViewModel.fetchTournamentNames()
            }
        }
    }

    LaunchedEffect(tournamentNames) {
        if (selectedCourt.isEmpty() && tournamentNames.isNotEmpty()) {
            onValueChange(tournamentNames.first())
        }
    }

    var expanded by remember { mutableStateOf(false) }

    MidDarkHeadline(text = "Court", size = 24)
    Spacer(modifier = Modifier.height(4.dp))

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedCourt,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            label = {
                Text(
                    "Select Court",
                    fontFamily = lexendFontFamily,
                    color = BlueDark
                )
            },
            singleLine = true,
            isError = isError,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown Icon",
                    tint = GreenLight
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (isError) Color.Red else GreenLight,
                unfocusedBorderColor = if (isError) Color.Red else GreenLight,
                cursorColor = GreenLight,
                focusedLabelColor = GreenLight,
                unfocusedLabelColor = GreenLight,
                errorBorderColor = Color.Red,
                errorCursorColor = Color.Red,
                errorLabelColor = Color.Red,
                disabledTextColor = BlueDark,
                focusedTextColor = BlueDark,
                unfocusedTextColor = BlueDark,
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            if (tournamentNames.isEmpty()) {
                DropdownMenuItem(
                    text = { Text("No favorite courts found", fontFamily = lexendFontFamily) },
                    onClick = {}
                )
            } else {
                tournamentNames.forEach { name ->
                    DropdownMenuItem(
                        text = { Text(name, fontFamily = lexendFontFamily) },
                        onClick = {
                            onValueChange(name)
                            expanded = false
                        },
                        trailingIcon = {
                            if (name == selectedCourt) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = GreenLight
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun PlayersGrid(players: List<FriendPlayer>) {
    MidDarkHeadline(text = "Players", size = 24)
    Spacer(modifier = Modifier.height(4.dp))

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        players.chunked(2).forEach { rowPlayers ->  // chunked to display 2 players per row
            Row(modifier = Modifier.padding(vertical = 4.dp)) {
                rowPlayers.forEachIndexed { index, player ->
                    // Determine button color based on whether the player's name is not empty
                    val isButtonFilled = player.name.isNotEmpty()

                    Button(
                        onClick = {},  // No onClick functionality
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isButtonFilled) GreenLight else WhiteGray
                        ),
                        border = BorderStroke(5.dp, if (isButtonFilled) GreenLight else GreenLight),
                        shape = RoundedCornerShape(100.dp),
                        elevation = ButtonDefaults.buttonElevation(0.dp),
                        modifier = Modifier
                            .weight(1f)
                            .width(100.dp)
                            .height(70.dp)
                            .padding(2.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            // Use painterResource to convert the photoResId (resource ID) to a Painter
                            Image(
                                painter = painterResource(id = player.photoResId),  // Convert resource ID to Painter
                                contentDescription = "Avatar",
                                contentScale = ContentScale.Inside,
                                modifier = Modifier
                                    .size(40.dp)
                                    .border(2.dp, BlueDark, CircleShape)
                                    .clip(CircleShape)
                                    .background(Transparent)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            // Player name
                            Text(
                                text = player.name,
                                fontSize = 16.sp,
                                color = BlueDark,  // Use BlueDark for text color
                                fontFamily = lexendFontFamily,
                                fontWeight = if (isButtonFilled) FontWeight.Bold else FontWeight.SemiBold,
                                maxLines = 1,
                            )
                        }
                    }
                }
            }
        }
    }
}



@Preview
@Composable
fun PlayerGridPreview(){
    PlayersGrid(players = listOf(FriendPlayer("Player 1", R.drawable.user), FriendPlayer("Player 2", R.drawable.user), FriendPlayer("Player 3", R.drawable.user), FriendPlayer("Player 4", R.drawable.user)))
}
