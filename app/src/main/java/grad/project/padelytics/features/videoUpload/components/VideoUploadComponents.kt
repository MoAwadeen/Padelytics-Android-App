package grad.project.padelytics.features.videoUpload.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.google.firebase.auth.FirebaseAuth
import grad.project.padelytics.R
import grad.project.padelytics.appComponents.MidDarkHeadline
import grad.project.padelytics.features.videoUpload.data.FriendData
import grad.project.padelytics.features.videoUpload.viewModel.FavouriteTournamentsViewModel
import grad.project.padelytics.features.videoUpload.viewModel.VideoUploadViewModel
import grad.project.padelytics.ui.theme.Blue
import grad.project.padelytics.ui.theme.BlueDark
import grad.project.padelytics.ui.theme.GreenDark
import grad.project.padelytics.ui.theme.GreenLight
import grad.project.padelytics.ui.theme.WhiteGray
import grad.project.padelytics.ui.theme.lexendFontFamily
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun VideoUploadCard(
    modifier: Modifier = Modifier.fillMaxWidth().fillMaxHeight(),
    initialVideoUri: Uri? = null,
    onVideoSelected: (Uri) -> Unit = {}
) {
    var videoUri by remember { mutableStateOf(initialVideoUri) }
    val context = LocalContext.current

    val videoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                videoUri = it
                onVideoSelected(it)
            }
        }
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .clickable { videoPickerLauncher.launch("video/*") },
        contentAlignment = Alignment.Center
    ) {
        when {
            videoUri != null -> {
                // Show video thumbnail with play icon overlay
                Box(contentAlignment = Alignment.Center) {
                    VideoThumbnail(
                        videoUri = videoUri!!,
                        modifier = Modifier.fillMaxSize()
                    )

                    Icon(
                        imageVector = Icons.Default.PlayCircle,
                        contentDescription = "Play video",
                        modifier = Modifier.size(48.dp),
                        tint = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
            else -> {
                // Show upload button
                Image(
                    painter = painterResource(id = R.drawable.upload_video),
                    contentDescription = "Upload video",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize(1f)
                )
            }
        }
    }
}

@Composable
fun VideoThumbnail(
    videoUri: Uri,
    modifier: Modifier = Modifier
) {
    val thumbnailPainter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(videoUri)
            .crossfade(true)
            .build(),
        placeholder = painterResource(R.drawable.upload_video),
        error = painterResource(R.drawable.info_circle)
    )

    AsyncImage(
        model = videoUri,
        contentDescription = "Video thumbnail",
        contentScale = ContentScale.Crop,
        modifier = modifier,
        placeholder = thumbnailPainter,
        error = thumbnailPainter
    )
}

@Preview(device = "id:pixel_4")
@Composable
fun VideoUploadPreview() {
    VideoUploadCard(){}
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
            containerColor = WhiteGray,
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
                        colors = MenuDefaults.itemColors(
                            textColor = BlueDark
                        ),
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
fun PlayersGrid(players: List<FriendData>, playerOnClick: () -> Unit = {}) {
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
                    val isButtonFilled = player.firstName.isNotEmpty()

                    Button(
                        onClick = playerOnClick,  // No onClick functionality
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
                            AsyncImage(
                                model = player.photo,
                                contentDescription = "Avatar",
                                contentScale = ContentScale.Inside,
                                modifier = Modifier
                                    .size(40.dp)
                                    .border(2.dp, BlueDark, CircleShape)
                                    .clip(CircleShape)
                                    .background(Transparent)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                           
                            Text(
                                text = player.firstName,
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
    PlayersGrid(players = listOf(
        FriendData(
            userName = "exampleUser",
            firstName = "John",
            lastName = "Doe",
            photo = "https://example.com/photo.jpg")
        ,FriendData(
            userName = "exampleUser",
            firstName = "",
            lastName = "Doe",
            photo = "https://example.com/photo.jpg")
        ,FriendData(
            userName = "",
            firstName = "John",
            lastName = "Doe",
            photo = "https://example.com/photo.jpg")
        ,FriendData(
            userName = "",
            firstName = "John",
            lastName = "Doe",
            photo = "https://example.com/photo.jpg")
    ))
}


@Composable
fun SearchFriendDialog(
    onDismiss: () -> Unit,
    onUserFound: (String) -> Unit,
    viewModel: VideoUploadViewModel = viewModel()
) {
    var username by remember { mutableStateOf("") }

    val searchResult by viewModel.searchResult.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(searchResult) {
        searchResult?.let { (username, docId) ->
            val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
            if (currentUserId != null) {
                viewModel.addUserToFriends(currentUserId, docId)
                onUserFound(username)
                viewModel.clearResult()
                onDismiss()
            }
        }
    }


    AlertDialog(
        containerColor = Blue,
        titleContentColor = BlueDark,
        textContentColor = GreenLight,
        onDismissRequest = {
            viewModel.clearResult()
            onDismiss()
        },
        confirmButton = {
            Button(
                onClick = {
                    viewModel.searchUsername(username)
                },
                enabled = username.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GreenLight,
                    contentColor = BlueDark
                )
            ) {
                Text(text = "Search",
                    fontFamily = lexendFontFamily,
                    fontWeight = FontWeight.SemiBold)
            }
        },
        dismissButton = {
            Button(onClick = {
                viewModel.clearResult()
                onDismiss()},
                colors = ButtonDefaults.buttonColors(
                    containerColor = GreenDark,
                    contentColor = GreenLight)) {
                Text(text = "Cancel",
                    fontFamily = lexendFontFamily,
                    fontWeight = FontWeight.SemiBold)
            }
        },
        title = { Text(
            text = "Find a Friend",
            fontFamily = lexendFontFamily,
            fontWeight = FontWeight.Bold,
            color = WhiteGray) },
        text = {
            Column {
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Enter username") },
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
                        errorTextColor = Color.Red)
                )

                if (isLoading) {
                    LinearProgressIndicator(
                        modifier = Modifier.padding(16.dp),
                        color = GreenLight,
                        trackColor = BlueDark,)
                } else {
                    when {
                        searchResult == null && username.isNotBlank() -> {
                            Text(
                                text = "User not found",
                                color = GreenDark,
                                modifier = Modifier.padding(top = 16.dp,end = 16.dp).align(Alignment.Start),
                                fontFamily = lexendFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp,
                            )
                        }
                    }
                }
            }
        }
    )
}

@Preview
@Composable
fun SearchFriendDialogPreview() {
    SearchFriendDialog(onDismiss = {}, onUserFound = {})
}

@Composable
fun FriendsListDialog(
    onDismiss: () -> Unit,
    onAdd: () -> Unit,
    onFriendClick: (FriendData) -> Unit, // New callback for friend clicks
    viewModel: VideoUploadViewModel = viewModel()
) {
    val friendsList by viewModel.friendsList.collectAsState()
    val isLoading by viewModel.isLoadingFriends.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchFriendsList()
    }

    AlertDialog(
        containerColor = Blue,
        titleContentColor = BlueDark,
        textContentColor = WhiteGray,
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = onAdd,
                colors = ButtonDefaults.buttonColors(
                    containerColor = GreenLight,
                    contentColor = BlueDark
                )) {
                Text(text = "Add",
                    fontFamily = lexendFontFamily,
                    fontWeight = FontWeight.SemiBold)
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss ,
                enabled = friendsList.all { it.photo.isNotBlank() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = GreenDark,
                    contentColor = GreenLight)
            ) {
                Text(text = "Close",
                    fontFamily = lexendFontFamily,
                    fontWeight = FontWeight.SemiBold)
            }
        },
        title = { Text(text = "Your Friends"
            ,fontFamily = lexendFontFamily,
            fontWeight = FontWeight.Bold,
            color = WhiteGray)  },
        text = {
            when {
                isLoading -> {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                friendsList.isEmpty() -> {
                    Text("You don't have any friends yet.",
                        color = GreenLight,
                        modifier = Modifier.padding(top = 16.dp,end = 16.dp),
                        fontFamily = lexendFontFamily,
                        fontWeight = FontWeight.SemiBold,)
                }
                else -> {
                    LazyColumn {
                        items(friendsList) { friend ->
                            FriendListItem(
                                friend = friend,
                                onClick = { onFriendClick(friend) } // Pass click to parent
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun FriendListItem(
    friend: FriendData,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() }, // Make whole row clickable
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = friend.photo,
            contentDescription = "${friend.userName}'s profile picture",
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = "${friend.firstName} ${friend.lastName}",
                color = WhiteGray,
                fontFamily = lexendFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = "@${friend.userName}",
                color = GreenLight,
                fontFamily = lexendFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp)
        }
    }
}

@Preview
@Composable
fun FriendsListDialogPreview() {
    FriendsListDialog(onDismiss = {}, onFriendClick = {}, onAdd = {})
}

@Preview(showBackground = true)
@Composable
fun FriendListItemPreview() {
    FriendListItem(
        friend = FriendData(
            userName = "exampleUser",
            firstName = "John",
            lastName = "Doe",
            photo = "https://example.com/photo.jpg"
        ),
        onClick = {}
    )
}
