package grad.project.padelytics.features.shop.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import grad.project.padelytics.appComponents.AppToolbar
import grad.project.padelytics.appComponents.BottomAppBar
import grad.project.padelytics.features.shop.components.ShopHeaders
import grad.project.padelytics.features.shop.components.ShopProduct
import grad.project.padelytics.features.shop.viewModel.ShopViewModel

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
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(color = Color.White)
                .padding(innerPadding)
                .padding(start = 20.dp, end = 20.dp, bottom = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                Spacer(modifier = Modifier.height(14.dp))

                ShopHeaders(
                    viewModel = viewModel,
                    selectedCategory = viewModel.selectedCategory.value,
                    selectedBrand = viewModel.selectedBrand.value,
                    selectedSorting = viewModel.selectedSorting.value
                )

                Spacer(modifier = Modifier.height(6.dp))
            }

            items(viewModel.products.value) { product ->
                product.delivery?.let {
                    product.product_star_rating?.let { it1 ->
                        ShopProduct(
                            viewModel = viewModel,
                            productId = product.asin,
                            productImage = product.product_photo,
                            productName = product.product_title,
                            productRating = it1,
                            productNumRating = product.product_num_ratings.toString(),
                            productDelivery = it,
                            productPrice = product.product_price,
                            productUrl = product.product_url
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShopScreenPreview() {
    ShopScreen(navController = rememberNavController())
}
