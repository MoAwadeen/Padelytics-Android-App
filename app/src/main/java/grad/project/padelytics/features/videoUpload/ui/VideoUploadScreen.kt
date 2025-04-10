package grad.project.padelytics.features.videoUpload.ui

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import grad.project.padelytics.R
import grad.project.padelytics.appComponents.AppToolbar
import grad.project.padelytics.appComponents.BottomAppBar
import grad.project.padelytics.appComponents.WideGreenButton
import grad.project.padelytics.features.profile.viewModel.ProfileViewModel
import grad.project.padelytics.features.videoUpload.components.CourtDropdownMenu
import grad.project.padelytics.features.videoUpload.components.FriendsListDialog
import grad.project.padelytics.features.videoUpload.components.PlayersGrid
import grad.project.padelytics.features.videoUpload.components.SearchFriendDialog
import grad.project.padelytics.features.videoUpload.components.VideoUploadCard
import grad.project.padelytics.features.videoUpload.data.FriendData
import grad.project.padelytics.features.videoUpload.viewModel.VideoUploadViewModel

@Composable
fun VideoUploadScreen(modifier: Modifier = Modifier,
                      navController: NavHostController,
                      profileViewModel: ProfileViewModel = viewModel(),
                      viewModel: VideoUploadViewModel = viewModel())
{
    val selectedVideo by viewModel.selectedVideoUri.collectAsState()
    var myName by remember { mutableStateOf("You") }
    var friendName1 by remember { mutableStateOf("") }
    var friendName2 by remember { mutableStateOf("") }
    var friendName3 by remember { mutableStateOf("") }

    val userProfile by profileViewModel.userProfile.collectAsState()
    val photo = userProfile?.photo ?: R.drawable.user
    val profileImage = rememberAsyncImagePainter(model = photo)


    var isBottomBarVisible by remember { mutableStateOf(true) }
    var selectedCourt by remember { mutableStateOf<String?>(null) }

    var showAddDialog by remember { mutableStateOf(false) }
    var showFriendsDialog by remember { mutableStateOf(false) }
    var selectedFriend by remember { mutableStateOf<FriendData?>(null) }

    if (showAddDialog) {
        SearchFriendDialog(
            onDismiss = { showAddDialog = false },
            onUserFound = { username ->
                Log.d("FriendSearch", "Found user: $username")
            }
        )
    }

    if (showFriendsDialog) {
        FriendsListDialog(
            onDismiss = { showFriendsDialog = false },
            onAdd = { showAddDialog = true },
            onFriendClick = { friend ->
                selectedFriend = friend
                Log.d("FriendSelected", "Selected: ${friend.userName}")
            }
        )
    }

    Scaffold(
        modifier = Modifier,
        topBar = {
            AppToolbar(toolbarTitle = "Upload Video")
        },
        bottomBar = {
            AnimatedVisibility(
                visible = isBottomBarVisible,
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(durationMillis = 300)
                ),
                exit = slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(durationMillis = 300)
                )
            ) {
                BottomAppBar(navController, navController.currentBackStackEntry?.destination?.route)
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                item {
                    VideoUploadCard(
                        onVideoSelected = { uri ->
                            viewModel.setSelectedVideo(uri)
                        }
                    )
                    Spacer(modifier = Modifier.height(22.dp))
                    PlayersGrid(playerOnClick = { showFriendsDialog = true },players =
                        listOf(
                            FriendData(
                                userName = "exampleUser",
                                firstName = myName,
                                lastName = "Doe",
                                photo = profileImage.toString()
                            )
                            ,FriendData(
                                userName = "exampleUser",
                                firstName = friendName1,
                                lastName = "Doe",
                                photo = profileImage.toString())
                            ,FriendData(
                                userName = "",
                                firstName = friendName2,
                                photo = profileImage.toString())
                            ,FriendData(
                                userName = "",
                                firstName = friendName3,
                                lastName = "Doe",
                                photo = profileImage.toString())
                        )
                    )

                    Spacer(modifier = Modifier.height(22.dp))
                    CourtDropdownMenu(selectedCourt = selectedCourt ?: "",
                        onValueChange = { selectedCourt = it })

                    Spacer(modifier = Modifier.height(44.dp))
                    WideGreenButton("Analyze", onClick = {})
                }
            }
        }
    }
}


@Preview
@Composable
fun VideoUploadScreenPreview(){
    VideoUploadScreen(navController = NavHostController(LocalContext.current))}