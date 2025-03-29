package grad.project.padelytics.features.home.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import grad.project.padelytics.appComponents.BottomAppBar
import grad.project.padelytics.features.home.components.HomeAppToolbar
import grad.project.padelytics.features.home.viewModel.HomeViewModel
import grad.project.padelytics.ui.theme.lexendFontFamily
import grad.project.padelytics.R
import grad.project.padelytics.appComponents.MidDarkHeadline
import grad.project.padelytics.features.home.components.FeatureList
import grad.project.padelytics.features.profile.components.NumberLabelChip
import grad.project.padelytics.navigation.Routes
import grad.project.padelytics.ui.theme.BlueDark
import grad.project.padelytics.ui.theme.GreenLight
import grad.project.padelytics.ui.theme.WhiteGray

@Composable
fun HomeScreen(modifier: Modifier = Modifier, navController: NavHostController, viewModel: HomeViewModel = viewModel()) {
    var userName by remember { mutableStateOf("User") }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    var isBottomBarVisible by remember { mutableStateOf(true) }
    var lastOffset by remember { mutableFloatStateOf(0f) }
    var isScrollingUp by remember { mutableStateOf(true) }

    val images = listOf(
        R.drawable.video,
        R.drawable.previousresults,
        R.drawable.court,
        R.drawable.shop,
        R.drawable.tournament
    )


    val titles = listOf(
        "Upload Video",
        "Previous Results",
        "Court Booking",
        "Shop",
        "Tournaments"
    )

    val actions = listOf(
        { navController.navigate(Routes.PROFILE) },
        { navController.navigate(Routes.PROFILE) },
        { navController.navigate(Routes.PROFILE) },
        { navController.navigate(Routes.PROFILE) }
    )

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                if (delta > 0) {
                    isScrollingUp = true
                } else if (delta < 0) {
                    isScrollingUp = false
                }
                lastOffset += delta
                isBottomBarVisible = isScrollingUp
                return Offset.Zero
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.fetchFirstName { name ->
            userName = name ?: "User"
        }
    }

    Scaffold(modifier = Modifier.nestedScroll(nestedScrollConnection),
        topBar = {
            HomeAppToolbar(userName = userName)
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
                BottomAppBar(navController, currentRoute)
            }
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
                FeatureList(navController)
            }
        }

        }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(navController = rememberNavController())
}
