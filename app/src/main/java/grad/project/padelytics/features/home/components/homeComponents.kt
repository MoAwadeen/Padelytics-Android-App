package grad.project.padelytics.features.home.components

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import grad.project.padelytics.R
import grad.project.padelytics.appComponents.FetchingIndicator
import grad.project.padelytics.appComponents.MidWhiteHeadline
import grad.project.padelytics.features.courtBooking.data.Court
import grad.project.padelytics.features.courtBooking.viewModel.CourtBookingViewModel
import grad.project.padelytics.features.shop.data.remote.model.Product
import grad.project.padelytics.features.shop.viewModel.ShopViewModel
import grad.project.padelytics.features.tournaments.data.Tournament
import grad.project.padelytics.features.tournaments.viewModel.TournamentsViewModel
import grad.project.padelytics.navigation.Routes
import grad.project.padelytics.ui.theme.Blue
import grad.project.padelytics.ui.theme.BlueDark
import grad.project.padelytics.ui.theme.GreenDark
import grad.project.padelytics.ui.theme.GreenLight
import grad.project.padelytics.ui.theme.WhiteGray
import grad.project.padelytics.ui.theme.lexendFontFamily
import kotlinx.coroutines.delay
import org.json.JSONObject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeAppToolbar(userName: String) {
    TopAppBar(
        modifier = Modifier.fillMaxWidth().height(80.dp),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Blue,
            titleContentColor = White,
        ),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.padelytics_2),
                    contentDescription = "Logo",
                    modifier = Modifier.width(160.dp).height(36.dp).padding(start = 3.dp)
                )
                Text(
                    text = "Hello, $userName !",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = lexendFontFamily,
                        fontWeight = FontWeight.Normal,
                        color = White
                    ),
                    modifier = Modifier.padding(end = 20.dp)
                )
            }
        }
    )
}

@Preview
@Composable
fun HomeAppToolbarPreview(){
    HomeAppToolbar(userName = "Merna")
}

@Composable
fun FeatureList(navController: NavController) {
    val features = listOf(
        Feature(R.drawable.video, "Upload\nVideo", Routes.VIDEO_UPLOAD),
        Feature(R.drawable.previousresults, "Previous\nResults", Routes.HOME),
        Feature(R.drawable.court, "Court\nBooking", Routes.COURTS),
        Feature(R.drawable.tournament, "Tournaments", Routes.TOURNAMENTS),
        Feature(R.drawable.shop, "Shop", Routes.SHOP)
    )

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top= 12.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        items(features) { feature ->
            FeatureItem(feature, navController)
        }
    }
}

@Composable
fun FeatureItem(feature: Feature,navController: NavController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(100.dp),
    ) {
        Card(
            modifier = Modifier
                .width(90.dp),
            shape = RoundedCornerShape(12.dp),
        ) {
        Image(
            painter = painterResource(id = feature.imageRes),
            contentDescription = feature.title,
            modifier = Modifier
                .size(90.dp)
                .clickable {navController.navigate(feature.route)},
            contentScale = ContentScale.FillHeight,

        )}

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = feature.title,
            fontSize = 14.sp,
            fontFamily = lexendFontFamily,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            color = BlueDark
        )
    }
}

data class Feature(
    val imageRes: Int,
    val title: String,
    val route: String
)

@Composable
fun Spotlight(
    modifier: Modifier = Modifier,
) {
    val url = "https://www.instagram.com/p/DFBSQymNMXH/?hl=en"
    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxWidth()
            .border(5.dp, Blue, RoundedCornerShape(16.dp))
            .background(color = Blue, shape = RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.spotlightwithad),
            contentDescription = "Upload video",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .clickable(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                        context.startActivity(intent)
                    }
                )
        )
    }
}

@Preview()
@Composable
fun SpotlightPreview() {
    Spotlight()
}

@Composable
fun MidGreenLightHeadline(text: String, size: Int){
    Text(text=text,
        fontSize = size.sp,
        color = GreenLight,
        fontFamily = lexendFontFamily,
        fontWeight = FontWeight.Bold)
}

@Preview
@Composable
fun ResultWidget(avatarImage: Painter = painterResource(id = R.drawable.loading_bg)){
    Box(
        modifier = Modifier
            .background(Blue, RoundedCornerShape(16.dp))
            .border(5.dp, BlueDark, RoundedCornerShape(16.dp)) // Border with rounded corners ) // Dark blue border
            .padding(8.dp) // Inner padding for content,
            .fillMaxWidth()
            .height(120.dp),
        contentAlignment = Alignment.Center

    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .align(Alignment.Start)
                    .height(40.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,

            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .shadow(8.dp, CircleShape)
                        .background(Transparent),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = avatarImage,
                        contentDescription = "Avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .border(2.dp, GreenLight, CircleShape)
                            .clip(CircleShape)
                            .background(Transparent)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Column(horizontalAlignment = Alignment.Start) {
                    MidWhiteHeadline("Name1",14)
                    Spacer(modifier = Modifier.weight(1f))
                    MidGreenLightHeadline("Level1",12)
                }

                Spacer(modifier = Modifier.weight(1f))

                MidWhiteHeadline("[ 3 - 6 ]",14)

                Spacer(modifier = Modifier.weight(1f))

                Column(horizontalAlignment = Alignment.End) {
                    MidWhiteHeadline("Name1",14)
                    Spacer(modifier = Modifier.weight(1f))
                    MidGreenLightHeadline("Level1",12)
                }

                Spacer(modifier = Modifier.weight(1f))

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .shadow(8.dp, CircleShape)
                        .background(Transparent),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = avatarImage,
                        contentDescription = "Avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .border(2.dp, GreenLight, CircleShape)
                            .clip(CircleShape)
                            .background(Transparent)
                    )
                }


            }
            //----------------------------------//
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    MidWhiteHeadline("21:00 Feb 18 2025",14)
                    MidWhiteHeadline("Padel-H Mansoura",14)
                }
                Spacer(modifier = Modifier.weight(1f))

                MidGreenLightHeadline("View Analysis",16)
            }
        }
    }
}

@Composable
fun HomeTitlesRow(featureTitle: String, showAll: String, onClick: () -> Unit){
    Row(modifier = Modifier.fillMaxWidth()
        .height(30.dp)
        .background(color = White),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
        )
    {
        Text(
            text = featureTitle,
            style = TextStyle(
                fontSize = 20.sp,
                fontFamily = lexendFontFamily,
                fontWeight = FontWeight.Bold,
                color = BlueDark
            )
        )

        Text(
            text = showAll,
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = lexendFontFamily,
                fontWeight = FontWeight.Medium,
                color = Blue
            ),
            modifier = Modifier.padding(end = 5.dp)
                .clickable { onClick() }
        )
    }
}

@Composable
fun HomeTournaments(tournament: Tournament, onClick: () -> Unit){
    Box(
        modifier = Modifier
            .width(147.dp)
            .height(202.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(12.dp),
                spotColor = Color.Gray.copy(alpha = 0.05f)
            )
            .drawBehind {
                drawRect(
                    color = Color.Gray.copy(alpha = 0.05f),
                    topLeft = Offset(4.dp.toPx(), size.height - 10.dp.toPx()),
                    size = Size(size.width - 8.dp.toPx(), 10.dp.toPx())
                )
            }
    ) {
        AsyncImage(
            modifier = Modifier
                .width(145.dp)
                .height(200.dp)
                .clip(RoundedCornerShape(12.dp)),
            model = tournament.image,
            contentDescription = "Tournament Image",
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center
        )
    }
}

@Composable
fun TournamentsList(tournamentsViewModel: TournamentsViewModel = viewModel(), navController: NavController){
    val tournaments by tournamentsViewModel.tournaments.collectAsState()
    val isFetching by tournamentsViewModel.isFetching.collectAsState()

    if (isFetching) {
        FetchingIndicator(modifier = Modifier.fillMaxSize(), isFetching = true)
    } else {
        LazyRow(Modifier.fillMaxWidth().background(color = White)) {
            items(tournaments.take(n = 5)) { tournament ->
                HomeTournaments(tournament = tournament,
                    onClick = { navController.navigate(route = "TOURNAMENT_DETAILS/${tournament.id}") }
                )

                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}

@Composable
fun HomeCourts(court: Court, onClick: () -> Unit){
    val context = LocalContext.current
    var isFavorite by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            containerColor = Blue,
            titleContentColor = BlueDark,
            textContentColor = GreenLight,
            onDismissRequest = { showDialog = false },
            title = {
                Text(text = "Remove from favorites",
                    style = TextStyle(
                        fontSize = 22.sp,
                        fontFamily = lexendFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = WhiteGray
                    ) )
            },
            text = {
                Text(text = "Are you sure you want to remove this court from your favorites?",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = lexendFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = BlueDark
                    ))
            },
            confirmButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GreenLight,
                        contentColor = BlueDark
                    ),
                    onClick = {
                        showDialog = false
                        CourtBookingViewModel().removeFavoriteCourt(court.courtId) { success ->
                            if (success) {
                                isFavorite = false
                                Toast.makeText(context, "Removed from favorites", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Failed to remove", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                ) {
                    Text(text = "YES",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = lexendFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = BlueDark
                        ))
                }
            },
            dismissButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GreenDark,
                        contentColor = GreenLight),
                    onClick = { showDialog = false }
                ) {
                    Text(text = "NO",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = lexendFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = GreenLight
                        ))
                }
            }
        )
    }

    LaunchedEffect(court.courtId) {
        CourtBookingViewModel().checkIfFavorite(court.courtId) { result ->
            isFavorite = result
        }
    }

    Column(modifier = Modifier
        .width(172.dp)
        .height(270.dp)
        .background(color = White)
        .clickable { onClick() },
        horizontalAlignment = Alignment.Start){

        Row(
            modifier = Modifier
                .width(172.dp)
                .height(172.dp)
                .padding(bottom = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(172.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .width(172.dp)
                        .height(172.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onClick() }
                        .shadow(
                            elevation = 4.dp,
                            shape = RoundedCornerShape(12.dp),
                            spotColor = Color.Gray.copy(alpha = 0.05f)
                        )
                        .drawBehind {
                            drawRect(
                                color = Color.Gray.copy(alpha = 0.05f),
                                topLeft = Offset(4.dp.toPx(), size.height - 10.dp.toPx()),
                                size = Size(size.width - 8.dp.toPx(), 10.dp.toPx())
                            )
                        }
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .width(170.dp)
                            .height(170.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        model = court.courtImage,
                        contentDescription = "Court Image",
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center
                    )
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .width(32.dp)
                        .height(32.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .padding(top = 4.dp, end = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = {
                            if (isFavorite) {
                                showDialog = true
                            } else {
                                CourtBookingViewModel().saveFavoriteCourt(court) { success ->
                                    if (success) {
                                        isFavorite = true
                                        Toast.makeText(context, "Added to favorites", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(context, "Failed to add", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        },
                        modifier = Modifier.size(30.dp)
                    ) {
                        Image(
                            painter = painterResource(
                                if (isFavorite) R.drawable.fav_selected else R.drawable.fav_unselected
                            ),
                            contentDescription = "Favorite",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(text = court.courtName,
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = lexendFontFamily,
                fontWeight = FontWeight.Bold,
                color = Blue
            ),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(text = court.bookingPrice,
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = lexendFontFamily,
                fontWeight = FontWeight.Bold,
                color = BlueDark
            ),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun CourtsList(courtBookingViewModel: CourtBookingViewModel = viewModel(), navController: NavController){
    val courts by courtBookingViewModel.courts.collectAsState()
    val isFetching by courtBookingViewModel.isFetching.collectAsState()

    if (isFetching) {
        FetchingIndicator(modifier = Modifier.fillMaxSize(), isFetching = true)
    } else {
        LazyRow(modifier = Modifier.fillMaxWidth().height(270.dp).background(color = White)){
            items(courts.take(n = 5)){court ->
                HomeCourts(
                    court = court,
                    onClick = { navController.navigate(route = "COURT_DETAILS/${court.courtId}") }
                )

                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}

@Composable
fun HomeProducts(product: Product,
                 productBrand:String,
                 productId: String,
                 productImage: String,
                 productName: String,
                 productRating: String,
                 productNumRating: String,
                 productPrice: String,
                 productDelivery: String,
                 productUrl: String,
                 productOffers: String,
                 onClick: () -> Unit){
    val context = LocalContext.current
    var isFavorite by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            containerColor = Blue,
            titleContentColor = BlueDark,
            textContentColor = GreenLight,
            onDismissRequest = { showDialog = false },
            title = {
                Text(text = "Remove from favorites",
                    style = TextStyle(
                        fontSize = 22.sp,
                        fontFamily = lexendFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = WhiteGray
                    ) )
            },
            text = {
                Text(text = "Are you sure you want to remove this product from your favorites?",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = lexendFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = BlueDark
                    ))
            },
            confirmButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GreenLight,
                        contentColor = BlueDark
                    ),
                    onClick = {
                        showDialog = false
                        ShopViewModel().removeFavoriteProduct(product.asin) { success ->
                            if (success) {
                                isFavorite = false
                                Toast.makeText(context, "Removed from favorites", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Failed to remove", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                ) {
                    Text(text = "YES",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = lexendFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = BlueDark
                        ))
                }
            },
            dismissButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GreenDark,
                        contentColor = GreenLight),
                    onClick = { showDialog = false }
                ) {
                    Text(text = "NO",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = lexendFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = GreenLight
                        ))
                }
            }
        )
    }

    LaunchedEffect(product.asin) {
        ShopViewModel().checkIfFavorite(product.asin) { result ->
            isFavorite = result
        }
    }

    Column(modifier = Modifier
        .width(172.dp)
        .height(220.dp)
        .background(color = White)
        .clickable { onClick() },
        horizontalAlignment = Alignment.Start){
        Row(
            modifier = Modifier
                .width(172.dp)
                .height(172.dp)
                .padding(bottom = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(172.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .width(172.dp)
                        .height(172.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(color = White)
                        .clickable { onClick() }
                        .shadow(
                            elevation = 4.dp,
                            shape = RoundedCornerShape(12.dp),
                            spotColor = Color.Gray.copy(alpha = 0.05f)
                        )
                        .drawBehind {
                            drawRect(
                                color = Color.Gray.copy(alpha = 0.05f),
                                topLeft = Offset(4.dp.toPx(), size.height - 10.dp.toPx()),
                                size = Size(size.width - 8.dp.toPx(), 10.dp.toPx())
                            )
                        }
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .width(170.dp)
                            .height(170.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(color = White),
                        model = product.product_photo,
                        contentDescription = "Product Image",
                        contentScale = ContentScale.Fit,
                        alignment = Alignment.Center
                    )
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .width(32.dp)
                        .height(32.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .padding(top = 4.dp, end = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = {
                            if (isFavorite) {
                                showDialog = true
                            } else {
                                ShopViewModel().saveFavoriteProduct(
                                    productId,
                                    productName,
                                    productPrice,
                                    productImage,
                                    productUrl,
                                    productRating,
                                    productNumRating,
                                    productDelivery,
                                    productOffers,
                                    productBrand
                                ) { success ->
                                    if (success) {
                                        isFavorite = true
                                        Toast.makeText(context, "Added to favorites", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(context, "Failed to add", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        },
                        modifier = Modifier.size(30.dp)
                    ) {
                        Image(
                            painter = painterResource(
                                if (isFavorite) R.drawable.fav_selected else R.drawable.fav_unselected
                            ),
                            contentDescription = "Favorite",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(text = productBrand,
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = lexendFontFamily,
                fontWeight = FontWeight.Bold,
                color = Blue
            ),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(text = product.product_price,
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = lexendFontFamily,
                fontWeight = FontWeight.Bold,
                color = BlueDark
            ),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun ProductsList(shopViewModel: ShopViewModel = viewModel(), navController: NavController) {
    val products by shopViewModel.products
    val isFetching by shopViewModel.isFetching.collectAsState()

    if (isFetching) {
        FetchingIndicator(modifier = Modifier.fillMaxWidth().height(270.dp), isFetching = true)
    } else {
        LazyRow(
            modifier = Modifier.fillMaxWidth().height(220.dp).background(color = White)
        ) {
            items(products.take(5)) { product ->
                if (product.delivery != null && product.product_star_rating != null){
                    val detectedBrand = shopViewModel.detectBrandFromMultiLangTitle(product.product_title)
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
                        put("productBrand", detectedBrand)
                    }.toString()

                    Spacer(modifier = Modifier.height(4.dp))

                    HomeProducts(
                        product = product,
                        productId = product.asin,
                        productBrand = detectedBrand,
                        productImage = product.product_photo,
                        productName = product.product_title,
                        productRating = product.product_star_rating ?: "0.0",
                        productNumRating = product.product_num_ratings.toString(),
                        productPrice = product.product_price,
                        productDelivery = product.delivery,
                        productUrl = product.product_url,
                        productOffers = product.product_num_offers.toString(),
                        onClick = {
                            sharedPrefs.edit{ putString("selected_product", productJson) }
                            navController.navigate(Routes.PRODUCT_DETAILS)
                        }
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}

@Composable
fun LazyRowSpotlight(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val spotlightItems = listOf(
        R.drawable.spotlightwithad to "https://www.instagram.com/p/DFBSQymNMXH/?hl=en",
        R.drawable.spotlight to "https://www.instagram.com/p/DFBSQymNMXH/?hl=en",
        R.drawable.spotlightad to "https://www.instagram.com/p/DFBSQymNMXH/?hl=en"
    )

    val listState = rememberLazyListState()
    var currentIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(timeMillis = 4000)
            currentIndex = (currentIndex + 1) % spotlightItems.size
            listState.animateScrollToItem(currentIndex)
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(5.dp, Blue, RoundedCornerShape(16.dp))
            .background(Blue)
    ) {
        LazyRow(
            state = listState,
            modifier = Modifier.fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
                .padding(horizontal = 5.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(spotlightItems) { (imageRes, link) ->
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = "Spotlight Image",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillParentMaxHeight()
                        .fillParentMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .clickable {
                            val intent = Intent(Intent.ACTION_VIEW, link.toUri())
                            context.startActivity(intent)
                        }
                )
            }
        }
    }
}