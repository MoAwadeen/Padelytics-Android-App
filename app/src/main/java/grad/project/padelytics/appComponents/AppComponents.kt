package grad.project.padelytics.appComponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import grad.project.padelytics.R
import grad.project.padelytics.navigation.Routes
import grad.project.padelytics.ui.theme.Blue
import grad.project.padelytics.ui.theme.BlueDark
import grad.project.padelytics.ui.theme.GreenLight
import grad.project.padelytics.ui.theme.WhiteGray
import grad.project.padelytics.ui.theme.lexendFontFamily

@Composable
fun MidWhiteHeadline(text: String,size: Int,){
    Text(text=text,
        fontSize = size.sp,
        color = WhiteGray,
        fontFamily = lexendFontFamily,
        fontWeight = FontWeight.Medium)
}

@Preview
@Composable
fun MidWhiteHeadlinePreview(){
    MidWhiteHeadline("Hello", 30)
}

@Composable
fun MidDarkHeadline(text: String,size: Int,){
    Text(text=text,
        fontSize = size.sp,
        color = BlueDark,
        fontFamily = lexendFontFamily,
        fontWeight = FontWeight.Bold)
}

@Composable
fun MidBlueHeadline(text: String,size: Int,){
    Text(text=text,
        fontSize = size.sp,
        color = Blue,
        fontFamily = lexendFontFamily,
        fontWeight = FontWeight.Bold)
}

@Composable
fun SimiMidDarkHeadline(text: String,size: Int,){
    Text(text=text,
        fontSize = size.sp,
        color = BlueDark,
        fontFamily = lexendFontFamily,
        fontWeight = FontWeight.SemiBold)
}

@Preview(showBackground = true)
@Composable
fun MidDarkHeadlinePreview(){
    MidDarkHeadline(" Mohamed ", 30)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppToolbar(toolbarTitle: String) {
    TopAppBar(
        modifier = Modifier.fillMaxWidth().height(80.dp),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Blue,
            titleContentColor = White,
        ),
        title = {
            Text(
                text = toolbarTitle,
                modifier = Modifier.fillMaxWidth().padding(top = 5.dp),
                style = TextStyle(
                    fontSize = 30.sp,
                    fontFamily = lexendFontFamily,
                    fontWeight = FontWeight.Medium,
                    color = White)
            )
        }
    )
}

@Preview
@Composable
fun AppToolbarPreview(){
    AppToolbar(toolbarTitle = "Home")
}

@Composable
fun BottomAppBar(navController: NavController, currentRoute: String?) {
    androidx.compose.material3.BottomAppBar(
        modifier = Modifier.fillMaxWidth().height(80.dp),
        containerColor = Blue,
        contentColor = White,
        tonalElevation = 16.dp,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        actions = {
            Row(
                modifier = Modifier.fillMaxSize().align(Alignment.Top).padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically

            ) {
                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    IconButton(
                        modifier = Modifier.size(24.dp),
                        onClick = { navController.navigate(Routes.HOME) }) {
                        val icon = if (currentRoute == Routes.HOME) {
                            painterResource(id = R.drawable.home_selected)
                        } else {
                            painterResource(id = R.drawable.home)
                        }
                        Image(
                            painter = icon,
                            contentDescription = "Home",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    val textColor = if (currentRoute == Routes.HOME){
                        GreenLight
                    }
                    else{
                        BlueDark
                    }
                    Text(
                        text = "Home",
                        style = TextStyle(
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = textColor
                        ),
                        textAlign = TextAlign.Center
                    )
                }
                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(
                        modifier = Modifier.size(24.dp),
                        onClick = { navController.navigate(Routes.FAVORITE) }) {
                        val icon = if (currentRoute == Routes.FAVORITE) {
                            painterResource(id = R.drawable.fav_unselected)
                        } else {
                            painterResource(id = R.drawable.fav)
                        }
                        Image(
                            painter = icon,
                            contentDescription = "Favorite",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    val textColor = if (currentRoute == Routes.FAVORITE){
                        GreenLight
                    }
                    else{
                        BlueDark
                    }
                    Text(
                        text = "Favorites",
                        style = TextStyle(
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = textColor
                        ),
                        textAlign = TextAlign.Center
                    )
                }
                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(
                        modifier = Modifier.size(24.dp),
                        onClick = { navController.navigate(Routes.PROFILE) }) {
                        val icon = if (currentRoute == Routes.PROFILE) {
                            painterResource(id = R.drawable.user_selected)
                        } else {
                            painterResource(id = R.drawable.user)
                        }
                        Image(
                            painter = icon,
                            contentDescription = "Profile",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    val textColor = if (currentRoute == Routes.PROFILE){
                        GreenLight
                    }
                    else{
                        BlueDark
                    }
                    Text(
                        text = "Account",
                        style = TextStyle(
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = textColor
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    )
}

@Preview
@Composable
fun BottomAppBarPreview() {
    val navController = rememberNavController()
    val currentRoute = Routes.HOME
    BottomAppBar(navController = navController, currentRoute = currentRoute)
}

@Composable
fun WideGreenButton(label: String, onClick: () -> Unit ){
    Button(onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(containerColor = GreenLight),
        modifier = Modifier.fillMaxWidth().height(60.dp),
        shape = RoundedCornerShape(100.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp))
    {
        Text(label,
            fontSize = 20.sp,
            color = BlueDark ,
            fontFamily = lexendFontFamily,
            fontWeight = FontWeight.SemiBold)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsAppToolbar(onClick: () -> Unit, itemName: String) {
    TopAppBar(
        modifier = Modifier.fillMaxWidth().height(80.dp),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Blue,
            titleContentColor = White,
        ),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { onClick() },
                    modifier = Modifier.size(40.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.back) ,
                        contentDescription = "Back",
                        modifier = Modifier.size(32.dp)
                    )
                }
                Text(
                    text = itemName,
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontFamily = lexendFontFamily,
                        fontWeight = FontWeight.Medium,
                        color = White
                    ),
                    modifier = Modifier.padding(start = 18.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    )
}

@Preview
@Composable
fun DetailsAppToolbarPreview(){
    DetailsAppToolbar(onClick = {}, itemName = "item Name")
}

@Composable
fun FetchingIndicator(
    modifier: Modifier = Modifier,
    isFetching: Boolean
) {
    if (isFetching) {
        val composition by rememberLottieComposition(
            LottieCompositionSpec.Asset("loading.json")
        )

        val progress by animateLottieCompositionAsState(
            composition,
            iterations = LottieConstants.IterateForever
        )

        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .align(Alignment.Center)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.loading_bg),
                    contentDescription = null,
                    modifier = Modifier
                        .matchParentSize()
                )

                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = Modifier
                        .size(100.dp)
                        .align(Alignment.Center)
                )

                Text(text = "Loading...",
                    modifier
                        .matchParentSize()
                        .padding(top = 90.dp)
                    ,
                    textAlign = TextAlign.Center,
                    color = WhiteGray,
                    fontFamily = lexendFontFamily,
                    fontSize = 14.sp)
            }
        }
    }
}

@Preview
@Composable
fun FetchingPreview(){
    FetchingIndicator(isFetching = true)
}
