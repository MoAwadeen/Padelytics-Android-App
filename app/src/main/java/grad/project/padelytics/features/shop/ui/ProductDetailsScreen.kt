package grad.project.padelytics.features.shop.ui

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import grad.project.padelytics.appComponents.BottomAppBar
import grad.project.padelytics.appComponents.DetailsAppToolbar
import grad.project.padelytics.features.profile.viewModel.ProfileViewModel
import grad.project.padelytics.features.shop.components.ProductDetails
import grad.project.padelytics.features.shop.viewModel.ShopViewModel
import org.json.JSONObject

@Composable
fun ProductDetailsScreen(modifier: Modifier = Modifier, navController: NavHostController, viewModel: ShopViewModel = viewModel(), profileViewModel: ProfileViewModel = viewModel()) {
    val context = LocalContext.current
    val sharedPrefs = context.getSharedPreferences("product_prefs", Context.MODE_PRIVATE)
    val productJson = sharedPrefs.getString("selected_product", null)
    val userProfile by profileViewModel.userProfile.collectAsState()

    if (productJson.isNullOrEmpty()) {
        Text("Product not found")
        return
    }

    val productObj = JSONObject(productJson)
    val productId = productObj.getString("productId")
    val productImage = productObj.getString("productImage")
    val productName = productObj.getString("productName")
    val productRating = productObj.getString("productRating")
    val productNumRating = productObj.getString("productNumRating")
    val productPrice = productObj.getString("productPrice")
    val productDelivery = productObj.getString("productDelivery")
    val productUrl = productObj.getString("productUrl")
    val productOffers = productObj.getString("productOffers")
    val userCity = userProfile?.city ?: "Unknown City"

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    var isBottomBarVisible by remember { mutableStateOf(true) }
    var lastOffset by remember { mutableFloatStateOf(0f) }
    var isScrollingUp by remember { mutableStateOf(true) }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                isScrollingUp = delta > 0
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
        profileViewModel.fetchUserProfile()
    }

    Scaffold(
        modifier = Modifier.nestedScroll(nestedScrollConnection),
        topBar = {
            DetailsAppToolbar(
                onClick = { navController.popBackStack() },
                itemName = ""
            )
        },
        bottomBar = {
            AnimatedVisibility(
                visible = isBottomBarVisible,
                enter = slideInVertically(initialOffsetY = { it }, animationSpec = tween(300)),
                exit = slideOutVertically(targetOffsetY = { it }, animationSpec = tween(300))
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
        ) {
            item {
                ProductDetails(
                    viewModel = viewModel,
                    productId = productId,
                    productImage = productImage,
                    productName = productName,
                    productRating = productRating,
                    productNumRating = productNumRating,
                    productPrice = productPrice,
                    productDelivery = productDelivery,
                    productUrl = productUrl,
                    productOffers = productOffers,
                    userCity = userCity
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductDetailsScreenPreview() {
    ProductDetailsScreen(navController = rememberNavController())
}
