package grad.project.padelytics.features.courtBooking.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import grad.project.padelytics.appComponents.BottomAppBar
import grad.project.padelytics.appComponents.DetailsAppToolbar
import grad.project.padelytics.appComponents.FetchingIndicator
import grad.project.padelytics.features.courtBooking.components.CourtDetails
import grad.project.padelytics.features.courtBooking.viewModel.CourtBookingViewModel

@Composable
fun CourtDetailsScreen(modifier: Modifier = Modifier, navController: NavHostController, viewModel: CourtBookingViewModel = viewModel(), courtId: String?) {
    val court by viewModel.getCourtById(courtId).collectAsState(initial = null)
    var isBottomBarVisible by remember { mutableStateOf(true) }
    var lastOffset by remember { mutableFloatStateOf(0f) }
    var isScrollingUp by remember { mutableStateOf(true) }
    val isFetching by viewModel.isFetching.collectAsState()

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
        navController.popBackStack()
    }

    LaunchedEffect(Unit) {
        viewModel.getCourtById(courtId)
    }

    Scaffold(
        modifier = Modifier.nestedScroll(nestedScrollConnection).fillMaxSize(),
        topBar = {
            DetailsAppToolbar(
                onClick = { navController.popBackStack() },
                itemName = court?.courtName ?: ""
            )
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
        if (isFetching) {
            FetchingIndicator(modifier = Modifier.fillMaxSize(), isFetching = true)
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.White)
                    .padding(innerPadding)
                    .pointerInput(Unit) {
                        detectVerticalDragGestures { _, dragAmount ->
                            if (dragAmount > 0) {
                                isBottomBarVisible = true
                            } else if (dragAmount < 0) {
                                isBottomBarVisible = false
                            }
                        }
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                item {
                    if (court != null) {
                        CourtDetails(court = court!!)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CourtDetailsScreenPreview() {
    CourtDetailsScreen(navController = rememberNavController(), courtId = "court1")
}