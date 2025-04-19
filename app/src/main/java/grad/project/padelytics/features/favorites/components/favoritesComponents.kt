package grad.project.padelytics.features.favorites.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import grad.project.padelytics.R
import grad.project.padelytics.features.favorites.data.FavoriteCourt
import grad.project.padelytics.features.favorites.data.FavoriteProduct
import grad.project.padelytics.features.favorites.data.FavoriteTournament
import grad.project.padelytics.ui.theme.Blue
import grad.project.padelytics.ui.theme.BlueDark
import grad.project.padelytics.ui.theme.lexendFontFamily

@Composable
fun FavoriteTitle(favoriteType: String){
    Text(
        text = "Favorite $favoriteType",
        style = TextStyle(
            fontSize = 20.sp,
            fontFamily = lexendFontFamily,
            fontWeight = FontWeight.Bold,
            color = BlueDark
        )
    )
}

@Preview(showBackground = true)
@Composable
fun FavoriteTitlePreview(){
    FavoriteTitle(favoriteType = "Tournament")
}

@Composable
fun FavoriteTournaments(favoriteTournament: FavoriteTournament, onClick: () -> Unit){
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
            model = favoriteTournament.image,
            contentDescription = "Tournament Image",
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center
        )
    }
}

@Composable
fun FavoriteCourts(favoriteCourt: FavoriteCourt, onClick: () -> Unit){
    Column(modifier = Modifier
        .width(172.dp)
        .clickable { onClick() },
        horizontalAlignment = Alignment.Start){
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
                model = favoriteCourt.courtImage,
                contentDescription = "Court Image",
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(text = favoriteCourt.courtName,
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

        Text(text = favoriteCourt.bookingPrice,
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
fun FavoriteProducts(favoriteProduct: FavoriteProduct, onClick: () -> Unit){
    Column(modifier = Modifier
        .width(172.dp)
        .clickable { onClick() },
        horizontalAlignment = Alignment.Start){
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
                model = favoriteProduct.productImage,
                contentDescription = "Product Image",
                contentScale = ContentScale.Fit,
                alignment = Alignment.Center
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(text = favoriteProduct.productBrand,
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

        Text(text = favoriteProduct.productPrice,
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
fun NoFavoritesAlert(){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.no_near_courts),
                contentDescription = "No Results",
                modifier = Modifier
                    .size(200.dp)
            )
            Text(
                text = "No Favorites",
                fontSize = 28.sp,
                fontFamily = lexendFontFamily,
                color = BlueDark,
                fontWeight = FontWeight.Bold
            )
        }
    }
}