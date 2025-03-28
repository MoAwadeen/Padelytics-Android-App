package grad.project.padelytics.appComponents

import androidx.collection.emptyLongSet
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PermIdentity
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import grad.project.padelytics.ui.theme.Blue
import grad.project.padelytics.ui.theme.BlueDark
import grad.project.padelytics.ui.theme.lexendFontFamily
import grad.project.padelytics.R
import grad.project.padelytics.navigation.Routes
import grad.project.padelytics.ui.theme.GreenLight
import grad.project.padelytics.ui.theme.WhiteGray
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

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
        modifier = Modifier.fillMaxWidth().height(70.dp),
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
                            painterResource(id = R.drawable.fav_selected)
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
