package grad.project.padelytics.features.courtBooking.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import grad.project.padelytics.appComponents.AppToolbar
import grad.project.padelytics.appComponents.BottomAppBar
import grad.project.padelytics.features.courtBooking.components.CourtHeaders
import grad.project.padelytics.features.courtBooking.components.CourtItem
import grad.project.padelytics.features.courtBooking.viewModel.CourtBookingViewModel
import grad.project.padelytics.navigation.Routes

@Composable
fun CourtsScreen(modifier: Modifier = Modifier, navController: NavHostController, viewModel: CourtBookingViewModel = viewModel()) {
    val courts by viewModel.courts.collectAsState()
    var isBottomBarVisible by remember { mutableStateOf(true) }
    var lastOffset by remember { mutableFloatStateOf(0f) }
    var isScrollingUp by remember { mutableStateOf(true) }

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

    BackHandler {
        navController.popBackStack(Routes.HOME, inclusive = false)
    }

    LaunchedEffect(Unit) {
        viewModel.fetchCourts()
    }

    Scaffold(
        modifier = Modifier.nestedScroll(nestedScrollConnection),
        topBar = {
            AppToolbar(toolbarTitle = "Court Booking")
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
                .padding(innerPadding)
                .padding(start = 20.dp, end = 20.dp)
                .pointerInput(Unit) {
                    detectVerticalDragGestures { _, dragAmount ->
                        if (dragAmount > 0) {
                            isBottomBarVisible = true
                        } else if (dragAmount < 0) {
                            isBottomBarVisible = false
                        }
                    }
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            var selectedValue by remember { mutableIntStateOf(2) }

            CourtHeaders(selectedValue = selectedValue, onValueChange = { selectedValue = it }, userCity = "City")

            Spacer(modifier = Modifier.height(6.dp))

            LazyColumn(
                modifier = Modifier.fillMaxWidth()
                .background(color = Color.White)
            ){
                items(courts){ court ->
                    CourtItem(
                        viewModel = CourtBookingViewModel(),
                        court = court,
                        courtId = court.courtId,
                        courtImage = court.courtImage,
                        courtName = court.courtName,
                        courtRating = court.courtRating,
                        courtNumRating = court.numRating,
                        courtPrice = court.bookingPrice,
                        onClick = {
                            navController.navigate(route = "COURT_DETAILS/${court.courtId}")
                        }
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
                item{
                    Spacer(modifier = Modifier.height(14.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CourtsScreenPreview() {
    CourtsScreen(navController = rememberNavController())
}