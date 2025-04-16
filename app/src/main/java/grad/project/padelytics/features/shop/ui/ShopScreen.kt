package grad.project.padelytics.features.shop.ui

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import grad.project.padelytics.appComponents.AppToolbar
import grad.project.padelytics.appComponents.BottomAppBar
import grad.project.padelytics.features.shop.components.ShopHeaders
import grad.project.padelytics.features.shop.components.ShopProduct
import grad.project.padelytics.features.shop.viewModel.ShopViewModel
import grad.project.padelytics.navigation.Routes
import org.json.JSONObject

@Composable
fun ShopScreen(modifier: Modifier = Modifier, navController: NavHostController, viewModel: ShopViewModel = viewModel()) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
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

    Scaffold(modifier = Modifier.nestedScroll(nestedScrollConnection),
        topBar = {
            AppToolbar(toolbarTitle = "Shop")
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
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(color = Color.White)
                .padding(innerPadding)
                .padding(start = 20.dp, end = 20.dp, bottom = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            ShopHeaders(
                viewModel = viewModel,
                selectedCategory = viewModel.selectedCategory.value,
                selectedBrand = viewModel.selectedBrand.value,
                selectedSorting = viewModel.selectedSorting.value
            )

            Spacer(modifier = Modifier.height(6.dp))

            LazyColumn(
                modifier = Modifier.fillMaxWidth()
                .background(color = Color.White)
            ){
                items(viewModel.products.value) { product ->
                    product.delivery?.let {
                        product.product_star_rating?.let { it1 ->
                            val context = LocalContext.current
                            val sharedPrefs = context.getSharedPreferences("product_prefs", Context.MODE_PRIVATE)
                            val productJson = JSONObject().apply {
                                put("productId", product.asin)
                                put("productImage", product.product_photo)
                                put("productName", product.product_title)
                                put("productRating", product.product_star_rating)
                                put("productNumRating", product.product_num_ratings.toString())
                                put("productPrice", product.product_price)
                                put("productDelivery", product.delivery)
                                put("productUrl", product.product_url)
                                put("productOffers", product.product_num_offers)
                            }.toString()

                            Spacer(modifier = Modifier.height(4.dp))

                            ShopProduct(
                                viewModel = viewModel,
                                productId = product.asin,
                                productImage = product.product_photo,
                                productName = product.product_title,
                                productRating = it1,
                                productNumRating = product.product_num_ratings.toString(),
                                productDelivery = it,
                                productPrice = product.product_price,
                                productUrl = product.product_url,
                                onClick = {
                                    sharedPrefs.edit{ putString("selected_product", productJson) }
                                    navController.navigate(Routes.PRODUCT_DETAILS)
                                }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShopScreenPreview() {
    ShopScreen(navController = rememberNavController())
}
