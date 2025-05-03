package grad.project.padelytics.features.home.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import grad.project.padelytics.appComponents.BottomAppBar
import grad.project.padelytics.features.courtBooking.viewModel.CourtBookingViewModel
import grad.project.padelytics.features.home.components.CourtsList
import grad.project.padelytics.features.home.components.FeatureList
import grad.project.padelytics.features.home.components.HomeAppToolbar
import grad.project.padelytics.features.home.components.HomeTitlesRow
import grad.project.padelytics.features.home.components.LazyRowSpotlight
import grad.project.padelytics.features.home.components.ProductsList
import grad.project.padelytics.features.home.components.TournamentsList
import grad.project.padelytics.features.home.viewModel.HomeViewModel
import grad.project.padelytics.features.results.components.ResultWidget
import grad.project.padelytics.features.shop.viewModel.ShopViewModel
import grad.project.padelytics.features.tournaments.viewModel.TournamentsViewModel
import grad.project.padelytics.navigation.Routes

@Composable
fun HomeScreen(modifier: Modifier = Modifier, navController: NavHostController, viewModel: HomeViewModel = viewModel()) {
    var userName by remember { mutableStateOf("User") }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    var isBottomBarVisible by remember { mutableStateOf(true) }
    var lastOffset by remember { mutableFloatStateOf(0f) }
    var isScrollingUp by remember { mutableStateOf(true) }
    val tournamentsViewModel: TournamentsViewModel = viewModel()
    val courtBookingViewModel: CourtBookingViewModel = viewModel()
    val shopViewModel: ShopViewModel = viewModel()

    BackHandler(enabled = true) { }

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
                .background(color = White)
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {  FeatureList(navController) }
            item {
                Column(
                    modifier = Modifier.background(color = White).
                    padding(horizontal = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    LazyRowSpotlight()

                    HomeTitlesRow(featureTitle = "Recent Matches",
                        showAll = "View All",
                        onClick = { navController.navigate(Routes.RESULTS) })

                    ResultWidget(navController = navController)

                    HomeTitlesRow(featureTitle = "Upcoming Tournaments",
                        showAll = "View All",
                        onClick = { navController.navigate(Routes.TOURNAMENTS) })

                    TournamentsList(tournamentsViewModel, navController)

                    HomeTitlesRow(featureTitle = "Courts Near You",
                        showAll = "Book Now",
                        onClick = { navController.navigate(Routes.COURTS) })

                    CourtsList(courtBookingViewModel, navController)

                    HomeTitlesRow(featureTitle = "Shop Your Equipments",
                        showAll = "Shop Now",
                        onClick = { navController.navigate(Routes.SHOP) })

                    ProductsList(shopViewModel, navController)

                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(navController = rememberNavController())
}