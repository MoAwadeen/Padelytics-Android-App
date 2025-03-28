package grad.project.padelytics.features.profile.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import grad.project.padelytics.R
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.rememberAsyncImagePainter
import grad.project.padelytics.appComponents.BottomAppBar
import grad.project.padelytics.features.auth.viewModel.AuthViewModel
import grad.project.padelytics.features.auth.components.WideGreenButton
import grad.project.padelytics.features.profile.viewModel.ProfileViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import grad.project.padelytics.appComponents.MidDarkHeadline
import grad.project.padelytics.features.profile.components.*
import grad.project.padelytics.ui.theme.BlueDark
import grad.project.padelytics.ui.theme.GreenLight
import grad.project.padelytics.ui.theme.WhiteGray

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier.background(WhiteGray),
    navController: NavHostController,
    authViewModel: AuthViewModel = viewModel(),
    profileViewModel: ProfileViewModel = viewModel(),
) {
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


    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            profileViewModel.uploadImage(it, context)
        }
    }

    fun selectImage() {
        pickImageLauncher.launch("image/*")
    }

    LaunchedEffect(Unit) {
        profileViewModel.fetchUserProfile()
    }

    Scaffold(
        modifier = Modifier.background(WhiteGray),
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
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                MidDarkHeadline(text = fullname, size = 20)

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
            }

            item {
                Column(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
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

                    InfoRow(
                        icon = painterResource(id = R.drawable.notifications),
                        label = "Notifications",
                    )

                    InfoRow(
                        icon = painterResource(id = R.drawable.coupon),
                        label = "Vouchers"
                    )

                    InfoRow(
                        icon = painterResource(id = R.drawable.add_user),
                        label = "Invite Friends"
                    )

                    InfoRow(
                        icon = painterResource(id = R.drawable.info_circle),
                        label = "About Us"
                    )

                    LogoutButton{
                        authViewModel.logout(navController)
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Preview
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(navController = NavHostController(LocalContext.current))
}
