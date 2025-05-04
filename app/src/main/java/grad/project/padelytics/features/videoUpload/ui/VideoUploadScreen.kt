package grad.project.padelytics.features.videoUpload.ui

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.runtime.mutableIntStateOf
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
import grad.project.padelytics.features.videoUpload.components.FirstFriendPlaceHolder
import grad.project.padelytics.features.videoUpload.components.FriendsListDialog
import grad.project.padelytics.features.videoUpload.components.MyPlayerPlaceHolder
import grad.project.padelytics.features.videoUpload.components.SearchFriendDialog
import grad.project.padelytics.features.videoUpload.components.SecondFriendPlaceHolder
import grad.project.padelytics.features.videoUpload.components.ThirdFriendPlaceHolder
import grad.project.padelytics.features.videoUpload.components.VideoUploadCard
import grad.project.padelytics.features.videoUpload.viewModel.VideoUploadViewModel

@Composable
fun VideoUploadScreen(modifier: Modifier = Modifier,
                      navController: NavHostController,
                      profileViewModel: ProfileViewModel = viewModel(),
                      viewModel: VideoUploadViewModel = viewModel())
{
    val selectedVideo by viewModel.selectedVideoUri.collectAsState()

    val userProfile by profileViewModel.userProfile.collectAsState()
    val userPhoto = userProfile?.photo ?: R.drawable.user
    val profileImage = rememberAsyncImagePainter(model = userPhoto)

    val isBottomBarVisible by remember { mutableStateOf(true) }
    var selectedCourt by remember { mutableStateOf<String?>(null) }

    var showAddDialog by remember { mutableStateOf(false) }
    var showFriendsDialog by remember { mutableStateOf(false) }

    val isFriendSelected by viewModel.isFriendSelected.collectAsState()

    val selectedFriends by viewModel.selectedFriends.collectAsState()
    var showFriendDialogIndex by remember { mutableIntStateOf(value = -1) }
    val selectedDialogIndex by remember { mutableIntStateOf(value = -1) }

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        profileViewModel.fetchUserProfile()
    }

    BackHandler {
        navController.popBackStack()
    }

    if (showAddDialog) {
        SearchFriendDialog(
            onDismiss = { showAddDialog = false },
            onUserFound = { username ->
                Log.d("FriendSearch", "Found user: $username")
            }
        )
    }

    if (showFriendsDialog && showFriendDialogIndex != -1) {
        FriendsListDialog(
            selectedIndex = selectedDialogIndex,
            onDismiss = {
                showFriendsDialog = false
                showFriendDialogIndex = -1
            },
            onFriendSelected = { friend ->
                viewModel.selectFriendAt(showFriendDialogIndex, friend)
                showFriendsDialog = false
                showFriendDialogIndex = -1
            },
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
                            modifier = Modifier.weight(0.5f,fill = true)
                        )

                        FirstFriendPlaceHolder(
                            friendName = selectedFriends.getOrNull(index = 0)?.firstName ?: "",
                            avatarImage = rememberAsyncImagePainter(model = selectedFriends.getOrNull(index = 0)?.photo ?: R.drawable.plus),
                            modifier = Modifier.weight(0.5f,fill = true),
                            onClick = {
                                showFriendDialogIndex = 0
                                showFriendsDialog = true
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ){
                        SecondFriendPlaceHolder(
                            friendName = selectedFriends.getOrNull(index = 1)?.firstName ?: "",
                            avatarImage = rememberAsyncImagePainter(model = selectedFriends.getOrNull(index = 1)?.photo ?: R.drawable.plus),
                            modifier = Modifier.weight(0.5f,fill = true),
                            onClick = {
                                showFriendDialogIndex = 1
                                showFriendsDialog = true
                            }
                        )

                        ThirdFriendPlaceHolder(
                            friendName = selectedFriends.getOrNull(index = 2)?.firstName ?: "",
                            avatarImage = rememberAsyncImagePainter(model = selectedFriends.getOrNull(index = 2)?.photo ?: R.drawable.plus),
                            modifier = Modifier.weight(0.5f,fill = true),
                            onClick = {
                                showFriendDialogIndex = 2
                                showFriendsDialog = true
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(22.dp))
                    CourtDropdownMenu(selectedCourt = selectedCourt ?: "",
                        onValueChange = { selectedCourt = it }
                    )
                }
                item {
                    WideGreenButton(label = "Analyze", onClick = {
                        val allFriendsSelected = selectedFriends.all { it != null }
                        if (!allFriendsSelected || selectedCourt.isNullOrEmpty()) {
                            Toast.makeText(context, "Please select 3 friends and a court", Toast.LENGTH_SHORT).show()
                        } else {
                            viewModel.saveMatchDetails(
                                selectedCourt = selectedCourt!!,
                                onSuccess = {
                                    Toast.makeText(context, "Match saved successfully", Toast.LENGTH_SHORT).show()
                                    navController.popBackStack()
                                },
                                onFailure = {
                                    Toast.makeText(context, "Failed to save match: ${it.message}", Toast.LENGTH_LONG).show()
                                }
                            )
                        }
                    }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun VideoUploadScreenPreview(){
    VideoUploadScreen(navController = NavHostController(LocalContext.current))}