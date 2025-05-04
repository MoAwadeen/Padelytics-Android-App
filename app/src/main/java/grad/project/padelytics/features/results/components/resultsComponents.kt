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
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import grad.project.padelytics.R
import grad.project.padelytics.appComponents.MidWhiteHeadline
import grad.project.padelytics.features.results.data.MatchData
import grad.project.padelytics.features.results.data.PlayerInfo
import grad.project.padelytics.navigation.Routes
import grad.project.padelytics.ui.theme.Blue
import grad.project.padelytics.ui.theme.BlueDark
import grad.project.padelytics.ui.theme.GreenLight
import grad.project.padelytics.ui.theme.lexendFontFamily

@Composable
fun ResultWidget(navController: NavController, matchData: MatchData){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Blue, RoundedCornerShape(16.dp))
            .border(5.dp, BlueDark, RoundedCornerShape(16.dp))
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                PlayerAvatar(matchData.players[0], GreenLight)

                Spacer(modifier = Modifier.weight(1f))

                PlayerAvatar(matchData.players[1], GreenLight)

                Spacer(modifier = Modifier.weight(1f))

                MidWhiteHeadline(text = "VS", size = 16)

                Spacer(modifier = Modifier.weight(1f))

                PlayerAvatar(matchData.players[2], BlueDark)

                Spacer(modifier = Modifier.weight(1f))

                PlayerAvatar(matchData.players[3], BlueDark)
            }

            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Bottom){
                MidWhiteHeadline(text = matchData.formattedTime, size = 14)

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    MidWhiteHeadline(text = matchData.court.limitWords(maxWords = 2), size = 14)

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text="View Analysis",
                        fontSize = 16.sp,
                        color = GreenLight,
                        fontFamily = lexendFontFamily,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End,
                        modifier = Modifier.clickable { navController.navigate(Routes.ANALYSIS) }
                    )
                }
            }
        }
    }
}

@Composable
fun PlayerAvatar(player: PlayerInfo, borderColor: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .shadow(8.dp, CircleShape)
                .background(Transparent),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = player.photo,
                contentDescription = "Player Photo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .border(2.dp, borderColor, CircleShape)
                    .clip(CircleShape)
                    .background(Transparent)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        MidWhiteHeadline(text = player.firstName, size = 14)
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

@Composable
fun NoMatchesAlert(){
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
                text = "No Uploaded Matches",
                fontSize = 28.sp,
                fontFamily = lexendFontFamily,
                color = BlueDark,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

fun String.limitWords(maxWords: Int): String {
    val words = this.trim().split("\\s+".toRegex())
    return if (words.size <= maxWords) this
    else words.take(maxWords).joinToString(" ") + "..."
}