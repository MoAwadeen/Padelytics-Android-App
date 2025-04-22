package grad.project.padelytics.features.analysis.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import grad.project.padelytics.R
import grad.project.padelytics.ui.theme.Blue
import grad.project.padelytics.ui.theme.GreenLight
import grad.project.padelytics.ui.theme.lexendFontFamily

@Composable
fun AnalysisHeader(){
    Box {
        Text(
            text = "MatchResults",
            style = TextStyle(
                fontSize = 24.sp,
                fontFamily = lexendFontFamily,
                fontWeight = FontWeight.Bold,
                color = Blue,
                drawStyle = Stroke(width = 14f)
            )
        )

        Text(
            text = "MatchResults",
            style = TextStyle(
                fontSize = 24.sp,
                fontFamily = lexendFontFamily,
                fontWeight = FontWeight.Bold,
                color = GreenLight
            )
        )
    }
}

@Composable
fun PlayersView(){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .height(200.dp),
        horizontalArrangement = Arrangement.SpaceBetween
        ) {
        Column(
            modifier = Modifier.fillMaxHeight()
                .wrapContentWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
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

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "You",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = lexendFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = Blue
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            Box(
                modifier = Modifier
                    .size(60.dp)
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

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Player",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = lexendFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = Blue
                )
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Column(modifier = Modifier.fillMaxHeight()
            .wrapContentWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box {
                Text(
                    text = "VS",
                    style = TextStyle(
                        fontSize = 30.sp,
                        fontFamily = lexendFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = Blue,
                        drawStyle = Stroke(width = 18f)
                    )
                )

                Text(
                    text = "VS",
                    style = TextStyle(
                        fontSize = 30.sp,
                        fontFamily = lexendFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = GreenLight
                    )
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Column(
            modifier = Modifier.fillMaxHeight()
                .wrapContentWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
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

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Player",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = lexendFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = Blue
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            Box(
                modifier = Modifier
                    .size(60.dp)
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

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Player",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = lexendFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = Blue
                )
            )
        }
    }
}

@Composable
fun AnalysisWideGreenButton(onClick: () -> Unit ){
    Button(
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(containerColor = GreenLight),
        modifier = Modifier.fillMaxWidth().height(50.dp),
        shape = RoundedCornerShape(10.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    )
    {
        Text(
            text = "Save As PDF",
            fontSize = 22.sp,
            color = Blue ,
            fontFamily = lexendFontFamily,
            fontWeight = FontWeight.Bold
        )
    }
}