package grad.project.padelytics.features.profile.ui

import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.rememberAsyncImagePainter
import grad.project.padelytics.R
import grad.project.padelytics.appComponents.BottomAppBar
import grad.project.padelytics.appComponents.MidBlueHeadline
import grad.project.padelytics.appComponents.MidDarkHeadline
import grad.project.padelytics.features.auth.viewModel.AuthViewModel
import grad.project.padelytics.features.profile.components.InfoRow
import grad.project.padelytics.features.profile.components.LogoutButton
import grad.project.padelytics.features.profile.components.LogoutConfirmationDialog
import grad.project.padelytics.features.profile.components.NotificationRow
import grad.project.padelytics.features.profile.components.ProfileHeader
import grad.project.padelytics.features.profile.viewModel.ProfileViewModel
import grad.project.padelytics.features.videoUpload.components.SearchFriendDialog
import grad.project.padelytics.navigation.Routes

@Composable
fun ProfileScreen(modifier: Modifier = Modifier, navController: NavHostController, authViewModel: AuthViewModel = viewModel(), profileViewModel: ProfileViewModel = viewModel(), ) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val context = LocalContext.current

    val userProfile by profileViewModel.userProfile.collectAsState()

    val fullname = "${userProfile?.firstName.orEmpty()} ${userProfile?.lastName.orEmpty()}".trim()
    val wins = userProfile?.wins ?: 0
    val losses = userProfile?.losses ?: 0
    val rank = userProfile?.rank ?: 0
    val conf = userProfile?.confidence ?: 1.5
    val photo = userProfile?.photo ?: R.drawable.user
    val city = userProfile?.city.orEmpty()
    val rewardPoints = userProfile?.rewardPoints ?: 0
    val userName = userProfile?.userName.orEmpty()

    var showAddDialog by remember { mutableStateOf(false) }
    var showConfirmationDialog by remember { mutableStateOf(false) }



    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            profileViewModel.uploadImage(it, context)
        }
    }

    BackHandler {
        navController.popBackStack()
    }

    fun selectImage() {
        pickImageLauncher.launch("image/*")
    }

    if (showAddDialog) {
        SearchFriendDialog(
            onDismiss = { showAddDialog = false },
            onUserFound = { username ->
                Log.d("FriendSearch", "Found user: $username")
            }
        )
    }

    if (showConfirmationDialog) {
        LogoutConfirmationDialog(
            showDialog = showConfirmationDialog,
            onDismiss = { showConfirmationDialog = false },
            onConfirmLogout = { authViewModel.logout(navController) }
        )
    }

    LaunchedEffect(Unit) {
        profileViewModel.fetchUserProfile()
        profileViewModel.updateRewardPointsBasedOnMatches()
    }

    Scaffold(
        modifier = Modifier.background(White),
        topBar = {
            val profileImage: Painter = rememberAsyncImagePainter(photo)
            ProfileHeader(avatarImage = profileImage, iconOnClick = {selectImage()})
        },
        bottomBar = {
            BottomAppBar(navController, currentRoute)
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(White)
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(50.dp))
                MidDarkHeadline(text = fullname, size = 20)
                MidBlueHeadline(text = "@$userName", size = 12)
/*
                Row(
                    modifier = Modifier.wrapContentSize(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    NumberLabelChip(
                        number = wins,
                        label = " Wins",
                        chipColor = BlueDark,
                        textColor = GreenLight
                    )
                    NumberLabelChip(
                        number = losses,
                        label = " Losses",
                        chipColor = BlueDark,
                        textColor = Color.Red
                    )
                    NumberLabelChip(
                        number = rank,
                        label = " Rank",
                        chipColor = BlueDark,
                        textColor = WhiteGray
                    )
                    NumberLabelChip(
                        number = conf.toInt(),
                        label = "% Conf",
                        chipColor = BlueDark,
                        textColor = WhiteGray
                    )
                }
 */
            }

            item {
                Column(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    InfoRow(
                        icon = painterResource(id = R.drawable.reward),
                        label = "Rewards",
                        value = "$rewardPoints Points"
                    )

                    InfoRow(
                        icon = painterResource(id = R.drawable.flag),
                        label = "City",
                        value = city
                    )

                    InfoRow(
                        icon = painterResource(id = R.drawable.language),
                        label = "Language",
                        value = "English"
                    )

                    NotificationRow()

                    InfoRow(
                        icon = painterResource(id = R.drawable.add_user),
                        label = "Add friends",
                        onClick = { showAddDialog = true }
                    )

                    InfoRow(
                        icon = painterResource(id = R.drawable.racket),
                        label = "About the game",
                        onClick = { navController.navigate(Routes.ABOUT_GAME) }
                    )

                    InfoRow(
                        icon = painterResource(id = R.drawable.info_circle),
                        label = "About us",
                        onClick = { navController.navigate(Routes.ABOUT_US) }
                    )

                }
            }
            item {
                Box(modifier = Modifier.padding(horizontal = 24.dp)){
                LogoutButton{
                    showConfirmationDialog = true
                }}
            }
        }
    }
}

@Preview
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(navController = NavHostController(LocalContext.current))
}
