package grad.project.padelytics.features.home.components

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavController
import grad.project.padelytics.R
import grad.project.padelytics.features.videoUpload.components.VideoThumbnail
import grad.project.padelytics.features.videoUpload.components.VideoUploadCard
import grad.project.padelytics.navigation.Routes
import grad.project.padelytics.ui.theme.Blue
import grad.project.padelytics.ui.theme.BlueDark
import grad.project.padelytics.ui.theme.lexendFontFamily

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
        Feature(R.drawable.court, "Court\nBooking", Routes.HOME),
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
        Card(modifier = Modifier
            .width(90.dp),
            shape = RoundedCornerShape(12.dp),) {
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
    modifier: Modifier = Modifier.fillMaxWidth(),
) {
    val url = "https://www.instagram.com/p/DFBSQymNMXH/?hl=en"
    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.spotlightwithad),
            contentDescription = "Upload video",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxSize(1f)
                .clickable(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
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
