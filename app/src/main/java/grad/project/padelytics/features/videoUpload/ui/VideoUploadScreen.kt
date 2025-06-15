package grad.project.padelytics.features.videoUpload.ui

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import grad.project.padelytics.R
import grad.project.padelytics.appComponents.AppToolbar
import grad.project.padelytics.appComponents.BottomAppBar
import grad.project.padelytics.appComponents.WideGreenButton
import grad.project.padelytics.data.MatchNotificationHelper
import grad.project.padelytics.features.profile.viewModel.ProfileViewModel
import grad.project.padelytics.features.videoUpload.components.AnalysisIndicator
import grad.project.padelytics.features.videoUpload.components.AnalysisResultDialog
import grad.project.padelytics.features.videoUpload.components.CourtBackground
import grad.project.padelytics.features.videoUpload.components.CourtDropdownMenu
import grad.project.padelytics.features.videoUpload.components.FriendsListDialog
import grad.project.padelytics.features.videoUpload.components.PlayerPlaceHolder
import grad.project.padelytics.features.videoUpload.components.PlayersTitleRow
import grad.project.padelytics.features.videoUpload.components.SearchFriendDialog
import grad.project.padelytics.features.videoUpload.components.VideoUploadCard
import grad.project.padelytics.features.videoUpload.viewModel.VideoUploadViewModel
import grad.project.padelytics.navigation.Routes
import grad.project.padelytics.ui.theme.BlueDark
import grad.project.padelytics.ui.theme.GreenLight

@SuppressLint("MissingPermission")
@Composable
fun VideoUploadScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    profileViewModel: ProfileViewModel = viewModel(),
    viewModel: VideoUploadViewModel = viewModel()
) {
    val isBottomBarVisible by remember { mutableStateOf(true) }
    var selectedCourt by remember { mutableStateOf<String?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }
    var showFriendsDialog by remember { mutableStateOf(false) }
    val selectedFriends by viewModel.selectedFriends.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var showFriendDialogIndex by remember { mutableIntStateOf(-1) }
    val selectedDialogIndex by remember { mutableIntStateOf(-1) }
    val context = LocalContext.current
    val showDialog by viewModel.showResultDialog.collectAsState()
    val resultUrl by viewModel.resultUrl.collectAsState()

    LaunchedEffect(Unit) {
        profileViewModel.fetchUserProfile()
    }

    BackHandler { navController.popBackStack() }

    if (showDialog) {
        AnalysisResultDialog(
            resultUrl = resultUrl,
            onDismiss = {
                viewModel.dismissDialog()
            }
        )
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
        topBar = { AppToolbar(toolbarTitle = "Upload Video") },
        bottomBar = {
            AnimatedVisibility(
                visible = isBottomBarVisible,
                enter = slideInVertically(initialOffsetY = { it }, animationSpec = tween(300)),
                exit = slideOutVertically(targetOffsetY = { it }, animationSpec = tween(300))
            ) {
                BottomAppBar(navController, navController.currentBackStackEntry?.destination?.route)
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start
            ) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    VideoUploadCard()
                    Spacer(modifier = Modifier.height(22.dp))
                    PlayersTitleRow()
                    Spacer(modifier = Modifier.height(8.dp))

                    CourtBackground {
                        val density = LocalDensity.current
                        val playerSize = 35.dp
                        val playerSizePx = with(density) { playerSize.toPx() }

                        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                            val courtWidth = constraints.maxWidth.toFloat()
                            val courtHeight = constraints.maxHeight.toFloat()
                            val x = courtWidth / 8f
                            val v1 = x
                            val v2 = courtWidth / 2f
                            val v3 = courtWidth - x
                            val horizontalY = courtHeight / 2f
                            val q1X = (v1 + v2) / 2f
                            val q2X = (v2 + v3) / 2f
                            val q1Y = horizontalY / 2f
                            val q3Y = (horizontalY + courtHeight) / 2f

                            fun px(x: Float): Dp = with(density) { x.toDp() }

                            val xShift = 8.dp
                            val yShift = 8.dp

                            PlayerPlaceHolder(
                                avatarImage = rememberAsyncImagePainter(model = selectedFriends.getOrNull(0)?.photo ?: R.drawable.plus_lg),
                                borderColor = GreenLight,
                                onClick = {
                                    showFriendDialogIndex = 0
                                    showFriendsDialog = true
                                },
                                modifier = Modifier.absoluteOffset(
                                    x = px(q1X - playerSizePx / 2) - xShift,
                                    y = px(q1Y - playerSizePx / 2) - yShift
                                )
                            )

                            PlayerPlaceHolder(
                                avatarImage = rememberAsyncImagePainter(model = selectedFriends.getOrNull(3)?.photo ?: R.drawable.plus_db),
                                borderColor = BlueDark,
                                onClick = {
                                    showFriendDialogIndex = 3
                                    showFriendsDialog = true
                                },
                                modifier = Modifier.absoluteOffset(
                                    x = px(q2X - playerSizePx / 2) - xShift,
                                    y = px(q1Y - playerSizePx / 2) - yShift
                                )
                            )

                            PlayerPlaceHolder(
                                avatarImage = rememberAsyncImagePainter(model = selectedFriends.getOrNull(1)?.photo ?: R.drawable.plus_lg),
                                borderColor = GreenLight,
                                onClick = {
                                    showFriendDialogIndex = 1
                                    showFriendsDialog = true
                                },
                                modifier = Modifier.absoluteOffset(
                                    x = px(q1X - playerSizePx / 2) - xShift,
                                    y = px(q3Y - playerSizePx / 2) - yShift
                                )
                            )

                            PlayerPlaceHolder(
                                avatarImage = rememberAsyncImagePainter(model = selectedFriends.getOrNull(2)?.photo ?: R.drawable.plus_db),
                                borderColor = BlueDark,
                                onClick = {
                                    showFriendDialogIndex = 2
                                    showFriendsDialog = true
                                },
                                modifier = Modifier.absoluteOffset(
                                    x = px(q2X - playerSizePx / 2) - xShift,
                                    y = px(q3Y - playerSizePx / 2) - yShift
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(22.dp))

                    CourtDropdownMenu(
                        selectedCourt = selectedCourt ?: "",
                        onValueChange = { selectedCourt = it }
                    )

                    Spacer(modifier = Modifier.height(22.dp))
                }

                item {
                    Box {
                        WideGreenButton(label = "Analyze") {
                            viewModel.uploadAndProcessVideo(
                                context,
                                onResult = { result ->
                                    Toast.makeText(context, result, Toast.LENGTH_LONG).show()
                                },
                                onResultReady = { resultUrl ->
                                    viewModel.dismissDialog()
                                    viewModel.saveMatchDetails(
                                        matchUri = resultUrl,
                                        selectedCourt = selectedCourt!!,
                                        onSuccess = { matchId ->
                                            Toast.makeText(context, "Match saved successfully", Toast.LENGTH_SHORT).show()
                                            MatchNotificationHelper.sendMatchSavedNotification(context, matchId)
                                            val sharedPrefs = context.getSharedPreferences("match_prefs", Context.MODE_PRIVATE)
                                            sharedPrefs.edit { putString("match_id", matchId) }
                                            navController.navigate(Routes.ANALYSIS)
                                        },
                                        onFailure = { exception ->
                                            Toast.makeText(context, "Failed to save match: ${exception.message}", Toast.LENGTH_SHORT).show()
                                        }
                                    )
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            AnalysisIndicator(modifier = Modifier.fillMaxSize(), isFetching = true)
        }
    }
}

@Preview
@Composable
fun VideoUploadScreenPreview() {
    VideoUploadScreen(navController = NavHostController(LocalContext.current))
}
