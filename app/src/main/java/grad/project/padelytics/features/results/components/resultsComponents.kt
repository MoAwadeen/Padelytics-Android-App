package grad.project.padelytics.features.results.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import grad.project.padelytics.R
import grad.project.padelytics.appComponents.MidWhiteHeadline
import grad.project.padelytics.navigation.Routes
import grad.project.padelytics.ui.theme.Blue
import grad.project.padelytics.ui.theme.BlueDark
import grad.project.padelytics.ui.theme.GreenLight
import grad.project.padelytics.ui.theme.lexendFontFamily
import okhttp3.Route

@Composable
fun MidGreenLightHeadline(text: String, size: Int){
    Text(text=text,
        fontSize = size.sp,
        color = GreenLight,
        fontFamily = lexendFontFamily,
        fontWeight = FontWeight.Bold)
}

@Composable
fun ResultWidget(navController: NavController){
    Box(
        modifier = Modifier
            .background(Blue, RoundedCornerShape(16.dp))
            .border(5.dp, BlueDark, RoundedCornerShape(16.dp))
            .padding(8.dp)
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
                        painter = painterResource(id = R.drawable.user_selected),
                        contentDescription = "Avatar",
                        contentScale = ContentScale.Fit,
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
                        painter = painterResource(id = R.drawable.user_selected),
                        contentDescription = "Avatar",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize()
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

                Text(
                    text="View Analysis",
                    fontSize = 16.sp,
                    color = GreenLight,
                    fontFamily = lexendFontFamily,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { navController.navigate(Routes.ANALYSIS) }
                )
            }
        }
    }
}

@Composable
fun HorizontalLine(color: Color, thickness: Float) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(thickness.dp)
    ) {
        drawLine(
            color = color,
            start = Offset(0f, size.height / 2),
            end = Offset(size.width, size.height / 2),
            strokeWidth = thickness
        )
    }
}