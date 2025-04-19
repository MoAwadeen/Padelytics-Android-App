package grad.project.padelytics.features.favorites.ui

import android.content.Context
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import grad.project.padelytics.appComponents.AppToolbar
import grad.project.padelytics.appComponents.BottomAppBar
import grad.project.padelytics.appComponents.FetchingIndicator
import grad.project.padelytics.features.favorites.components.FavoriteCourts
import grad.project.padelytics.features.favorites.components.FavoriteProducts
import grad.project.padelytics.features.favorites.components.FavoriteTitle
import grad.project.padelytics.features.favorites.components.FavoriteTournaments
import grad.project.padelytics.features.favorites.components.NoFavoritesAlert
import grad.project.padelytics.features.favorites.viewModel.FavoritesViewModel
import grad.project.padelytics.navigation.Routes
import org.json.JSONObject

@Composable
fun FavoritesScreen(modifier: Modifier = Modifier, navController: NavHostController, viewModel: FavoritesViewModel = viewModel()) {
    var isBottomBarVisible by remember { mutableStateOf(true) }
    var lastOffset by remember { mutableFloatStateOf(0f) }
    var isScrollingUp by remember { mutableStateOf(true) }
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    val favoriteTournaments by viewModel.favoriteTournaments.collectAsState()
    val favoriteCourts by viewModel.favoriteCourts.collectAsState()
    val favoriteProducts by viewModel.favoriteProducts.collectAsState()
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
        if (userId != null) {
            viewModel.fetchFavoriteTournaments(userId = userId)
            viewModel.fetchFavoriteCourts(userId = userId)
            viewModel.fetchFavoriteProducts(userId = userId)
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(nestedScrollConnection),
        topBar = {
            AppToolbar(toolbarTitle = "Favorites")
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
            if (favoriteTournaments.isEmpty() && favoriteCourts.isEmpty() && favoriteProducts.isEmpty()){
                NoFavoritesAlert()
            }
            else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color.White)
                        .padding(innerPadding)
                        .padding(start = 20.dp, end = 20.dp, top = 12.dp)
                        .pointerInput(Unit) {
                            detectVerticalDragGestures { _, dragAmount ->
                                if (dragAmount > 0) {
                                    isBottomBarVisible = true
                                } else if (dragAmount < 0) {
                                    isBottomBarVisible = false
                                }
                            }
                        },
                    horizontalAlignment = Alignment.Start
                ) {
                    LazyColumn(modifier = Modifier.fillMaxSize()
                        .background(color = Color.White),
                        horizontalAlignment = Alignment.Start){
                        if (favoriteTournaments.isNotEmpty()){
                            item {
                                FavoriteTitle(favoriteType = "Tournaments")

                                Spacer(modifier = Modifier.height(12.dp))

                                LazyRow {
                                    items(favoriteTournaments){ favoriteTournament ->
                                        FavoriteTournaments(
                                            favoriteTournament = favoriteTournament,
                                            onClick = {
                                                navController.navigate("TOURNAMENT_DETAILS/${favoriteTournament.id}")
                                            }
                                        )

                                        Spacer(modifier = Modifier.width(8.dp))
                                    }
                                }

                                Spacer(modifier = Modifier.height(20.dp))
                            }
                        }

                        if (favoriteCourts.isNotEmpty()){
                            item {
                                FavoriteTitle(favoriteType = "Courts")

                                Spacer(modifier = Modifier.height(12.dp))

                                LazyRow {
                                    items(favoriteCourts){ favoriteCourt ->
                                        FavoriteCourts(
                                            favoriteCourt = favoriteCourt,
                                            onClick = {
                                                navController.navigate(route = "COURT_DETAILS/${favoriteCourt.courtId}")
                                            }
                                        )

                                        Spacer(modifier = Modifier.width(8.dp))
                                    }
                                }

                                Spacer(modifier = Modifier.height(20.dp))
                            }
                        }

                        if (favoriteProducts.isNotEmpty()){
                            item {
                                FavoriteTitle(favoriteType = "Products")

                                Spacer(modifier = Modifier.height(12.dp))

                                LazyRow {
                                    items(favoriteProducts){ favoriteProduct ->
                                        val context = LocalContext.current
                                        val sharedPrefs = context.getSharedPreferences("product_prefs", Context.MODE_PRIVATE)
                                        val productJson = JSONObject().apply {
                                            put("productId", favoriteProduct.productId)
                                            put("productImage", favoriteProduct.productImage)
                                            put("productName", favoriteProduct.productName)
                                            put("productRating", favoriteProduct.productRating)
                                            put("productNumRating", favoriteProduct.productNumRating)
                                            put("productPrice", favoriteProduct.productPrice)
                                            put("productDelivery", favoriteProduct.productDelivery)
                                            put("productUrl", favoriteProduct.productUrl)
                                            put("productOffers", favoriteProduct.productOffers)
                                        }.toString()

                                        FavoriteProducts(
                                            favoriteProduct = favoriteProduct,
                                            onClick = {
                                                sharedPrefs.edit{ putString("selected_product", productJson) }
                                                navController.navigate(Routes.PRODUCT_DETAILS)
                                            }
                                        )

                                        Spacer(modifier = Modifier.width(8.dp))
                                    }
                                }

                                Spacer(modifier = Modifier.height(20.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun FavoritesScreenPreview() {
    FavoritesScreen(navController = NavHostController(LocalContext.current))
}