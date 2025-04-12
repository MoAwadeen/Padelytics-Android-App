package grad.project.padelytics.features.videoUpload.ui

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import grad.project.padelytics.appComponents.MidDarkHeadline
import grad.project.padelytics.appComponents.WideGreenButton
import grad.project.padelytics.features.profile.viewModel.ProfileViewModel
import grad.project.padelytics.features.videoUpload.components.CourtDropdownMenu
import grad.project.padelytics.features.videoUpload.components.FriendPlaceHolder
import grad.project.padelytics.features.videoUpload.components.FriendsListDialog
import grad.project.padelytics.features.videoUpload.components.MyPlayerPlaceHolder
import grad.project.padelytics.features.videoUpload.components.SearchFriendDialog
import grad.project.padelytics.features.videoUpload.components.VideoUploadCard
import grad.project.padelytics.features.videoUpload.viewModel.VideoUploadViewModel

@Composable
fun VideoUploadScreen(modifier: Modifier = Modifier,
                      navController: NavHostController,
                      profileViewModel: ProfileViewModel = viewModel(),
                      viewModel: VideoUploadViewModel = viewModel())
{
    val selectedVideo by viewModel.selectedVideoUri.collectAsState()
    val selectedFriend by viewModel.selectedFriend.collectAsState()
    val friendName = selectedFriend?.firstName ?: ""
    val friendUserName = selectedFriend?.userName ?: ""
    val friendPhoto = selectedFriend?.photo ?: R.drawable.plus
    val friendProfileImage = rememberAsyncImagePainter(model = friendPhoto)

    val userProfile by profileViewModel.userProfile.collectAsState()
    val userPhoto = userProfile?.photo ?: R.drawable.user
    val profileImage = rememberAsyncImagePainter(model = userPhoto)


    var isBottomBarVisible by remember { mutableStateOf(true) }
    var selectedCourt by remember { mutableStateOf<String?>(null) }

    var showAddDialog by remember { mutableStateOf(false) }
    var showFriendsDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        profileViewModel.fetchUserProfile()
    }

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
            onAddFriendClick = { showAddDialog = true }
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
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start
            ) {
                item {
                    VideoUploadCard(
                        onVideoSelected = { uri ->
                            viewModel.setSelectedVideo(uri)
                        }
                    )
                    Spacer(modifier = Modifier.height(22.dp))
                    MidDarkHeadline("Players", size = 24)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ){
                        MyPlayerPlaceHolder(
                            avatarImage = profileImage,
                            modifier = Modifier.weight(0.5f,fill = true))

                        FriendPlaceHolder(
                            friendName = friendName,
                            onClick = { showFriendsDialog = true }
                            ,avatarImage = friendProfileImage
                            ,modifier = Modifier.weight(0.5f,fill = true))
                    }

                    Spacer(modifier = Modifier.height(22.dp))
                    CourtDropdownMenu(selectedCourt = selectedCourt ?: "",
                        onValueChange = { selectedCourt = it })

                }
                item {
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