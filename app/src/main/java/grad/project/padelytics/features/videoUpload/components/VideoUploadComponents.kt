package grad.project.padelytics.features.videoUpload.components

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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
import grad.project.padelytics.features.favorites.viewModel.FavoritesViewModel
import grad.project.padelytics.features.videoUpload.data.FriendData
import grad.project.padelytics.features.videoUpload.viewModel.VideoUploadViewModel
import grad.project.padelytics.ui.theme.Blue
import grad.project.padelytics.ui.theme.BlueDark
import grad.project.padelytics.ui.theme.GreenDark
import grad.project.padelytics.ui.theme.GreenLight
import grad.project.padelytics.ui.theme.WhiteGray
import grad.project.padelytics.ui.theme.lexendFontFamily

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
    viewModel: FavoritesViewModel = viewModel()
) {
    val favoriteCourts by viewModel.favoriteCourts.collectAsState()
    val isFetching by viewModel.isFetching.collectAsState()
    val userId = FirebaseAuth.getInstance().currentUser?.uid

    // Automatically fetch once
    LaunchedEffect(Unit) {
        if (userId != null) {
            viewModel.fetchFavoriteCourts(userId)
        }
    }

    // Auto-select first court if none selected
    LaunchedEffect(favoriteCourts) {
        if (selectedCourt.isEmpty() && favoriteCourts.isNotEmpty()) {
            onValueChange(favoriteCourts.first().courtName)
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
            when {
                isFetching -> {
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    strokeWidth = 2.dp,
                                    color = GreenLight
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Loading courts...", fontFamily = lexendFontFamily)
                            }
                        },
                        onClick = {}
                    )
                }

                favoriteCourts.isEmpty() -> {
                    DropdownMenuItem(
                        text = { Text("No favorite courts found", fontFamily = lexendFontFamily) },
                        onClick = {}
                    )
                }

                else -> {
                    favoriteCourts.forEach { court ->
                        DropdownMenuItem(
                            text = { Text(court.courtName, fontFamily = lexendFontFamily) },
                            onClick = {
                                onValueChange(court.courtName)
                                expanded = false
                            },
                            trailingIcon = {
                                if (court.courtName == selectedCourt) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = GreenLight
                                    )
                                }
                            },
                            colors = MenuDefaults.itemColors(
                                textColor = BlueDark
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MyPlayerPlaceHolder(
    avatarImage: Painter = painterResource(id = R.drawable.user),
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = GreenLight,
            contentColor = BlueDark
        ),
        shape = RoundedCornerShape(60.dp),
        border = BorderStroke(3.dp, GreenLight),
        elevation = ButtonDefaults.buttonElevation(0.dp),
        modifier = modifier
            .height(50.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = avatarImage,
                contentDescription = "Avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(35.dp)
                    .border(3.dp, BlueDark, CircleShape)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = "You",
                fontSize = 16.sp,
                color = BlueDark,
                fontFamily = lexendFontFamily,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun FirstFriendPlaceHolder(
    avatarImage: Painter = painterResource(id = R.drawable.plus),
    friendName: String = "",
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (friendName.isNotEmpty()) GreenDark else WhiteGray,
            contentColor = GreenLight
        ),
        shape = RoundedCornerShape(60.dp),
        border = BorderStroke(3.dp, GreenDark),
        elevation = ButtonDefaults.buttonElevation(0.dp),
        modifier = modifier
            .height(50.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth().wrapContentSize()
        ) {
            Text(
                text = friendName,
                fontSize = 14.sp,
                color = GreenLight,
                fontFamily = lexendFontFamily,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,

            )

            Spacer(modifier = Modifier.width(6.dp))

            Image(
                painter = avatarImage,
                contentDescription = "Avatar",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(35.dp)
                    .border(3.dp, GreenLight, CircleShape)
                    .clip(CircleShape)
            )
        }
    }
}

@Composable
fun SecondFriendPlaceHolder(
    avatarImage: Painter = painterResource(id = R.drawable.plus),
    friendName: String = "",
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (friendName.isNotEmpty()) GreenDark else WhiteGray,
            contentColor = GreenLight
        ),
        shape = RoundedCornerShape(60.dp),
        border = BorderStroke(3.dp, GreenDark),
        elevation = ButtonDefaults.buttonElevation(0.dp),
        modifier = modifier
            .height(50.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth().wrapContentSize()
        ) {
            Image(
                painter = avatarImage,
                contentDescription = "Avatar",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(35.dp)
                    .border(3.dp, GreenLight, CircleShape)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = friendName,
                fontSize = 14.sp,
                color = GreenLight,
                fontFamily = lexendFontFamily,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun ThirdFriendPlaceHolder(
    avatarImage: Painter = painterResource(id = R.drawable.plus),
    friendName: String = "",
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (friendName.isNotEmpty()) GreenDark else WhiteGray,
            contentColor = GreenLight
        ),
        shape = RoundedCornerShape(60.dp),
        border = BorderStroke(3.dp, GreenDark),
        elevation = ButtonDefaults.buttonElevation(0.dp),
        modifier = modifier
            .height(50.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth().wrapContentSize()
        ) {
            Text(
                text = friendName,
                fontSize = 14.sp,
                color = GreenLight,
                fontFamily = lexendFontFamily,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.width(6.dp))

            Image(
                painter = avatarImage,
                contentDescription = "Avatar",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(35.dp)
                    .border(3.dp, GreenLight, CircleShape)
                    .clip(CircleShape)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyPlayerPlaceHolderPreview(){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ){
    MyPlayerPlaceHolder(modifier = Modifier.weight(1f))

    FirstFriendPlaceHolder(friendName = "",modifier = Modifier.weight(1f))}
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
                Text(text = "ADD",
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
                Text(text = "CANCEL",
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
    selectedIndex: Int,
    onDismiss: () -> Unit,
    onFriendSelected: (FriendData) -> Unit,
    onAddFriendClick: () -> Unit,
    viewModel: VideoUploadViewModel = viewModel()
) {
    val friendsList by viewModel.friendsList.collectAsState()
    val isLoading by viewModel.isLoadingFriends.collectAsState()
    val selectedFriends by viewModel.selectedFriends.collectAsState()
    val context = LocalContext.current
    val selectedFriend = selectedFriends.getOrNull(selectedIndex)

    LaunchedEffect(Unit) {
        viewModel.fetchFriendsList()
    }

    AlertDialog(
        modifier = Modifier.padding(vertical = 16.dp),
        containerColor = Blue,
        titleContentColor = WhiteGray,
        textContentColor = WhiteGray,
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Select a Friend",
                fontFamily = lexendFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                selectedFriend?.let { friend ->
                    SelectedFriendPreview(
                        friend = friend,
                        onRemove = {
                            viewModel.unselectFriendAt(selectedIndex)
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(color = GreenLight.copy(alpha = 0.3f))
                    Spacer(modifier = Modifier.height(8.dp))
                }

                when {
                    isLoading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = GreenLight)
                        }
                    }

                    friendsList.isEmpty() -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No friends found",
                                color = GreenLight,
                                fontFamily = lexendFontFamily
                            )
                        }
                    }

                    else -> {
                        val availableFriends = friendsList.filter { friend ->
                            selectedFriends.none { it?.userName == friend.userName }
                        }

                        LazyColumn(modifier = Modifier.heightIn(max = 400.dp)) {
                            items(availableFriends) { friend ->
                                FriendListItem(
                                    friend = friend,
                                    onClick = {
                                        viewModel.selectFriendAt(selectedIndex, friend)
                                        onFriendSelected(friend)
                                        onDismiss()
                                    }
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (selectedFriend != null) {
                        onDismiss()
                    } else {
                        Toast.makeText(context, "Please select a friend", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = GreenLight,
                    contentColor = BlueDark
                )
            ) {
                Text(
                    text = "DONE",
                    fontFamily = lexendFontFamily,
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        dismissButton = {
            Button(
                onClick = onAddFriendClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = GreenDark,
                    contentColor = GreenLight
                )
            ) {
                Text(
                    text = "ADD PLAYER",
                    fontFamily = lexendFontFamily,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    )
}

@Composable
private fun SelectedFriendPreview(friend: FriendData, onRemove: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onRemove() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = friend.photo,
            contentDescription = "Selected friend",
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = "@${friend.userName}",
                color = WhiteGray,
                fontFamily = lexendFontFamily,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "${friend.firstName} ${friend.lastName}",
                color = GreenLight,
                fontFamily = lexendFontFamily,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun FriendListItem(friend: FriendData, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Transparent
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = friend.photo,
                contentDescription = "Friend avatar",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "@${friend.userName}",
                    color = WhiteGray,
                    fontFamily = lexendFontFamily,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${friend.firstName} ${friend.lastName}",
                    color = GreenLight.copy(alpha = 0.85f),
                    fontFamily = lexendFontFamily,
                    fontSize = 12.sp
                )
            }
        }
    }
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