package grad.project.padelytics.features.tournaments.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import grad.project.padelytics.R
import grad.project.padelytics.features.tournaments.data.Tournament
import grad.project.padelytics.ui.theme.Blue
import grad.project.padelytics.ui.theme.BlueDark
import grad.project.padelytics.ui.theme.lexendFontFamily

@Composable
fun GridItem(tournament: String, prize: String, date: String, onClick: () -> Unit){
    Column(modifier = Modifier.fillMaxWidth()) {

        AsyncImage(
            modifier = Modifier
                .width(190.dp)
                .height(240.dp)
                .clip(RoundedCornerShape(12.dp))
                .clickable { onClick() },
            model = ImageRequest.Builder(LocalContext.current)
                .data(R.drawable.tournament)
                .crossfade(true)
                .scale(Scale.FILL)
                .build(),
            contentDescription = "Image",
            contentScale = ContentScale.Crop
        )

        Text(
            text = tournament,
            modifier = Modifier.fillMaxWidth(),
            style = TextStyle(
                fontSize = 24.sp,
                fontFamily = lexendFontFamily,
                fontWeight = FontWeight.Bold,
                color = Blue
            )
        )
        
        Text(
            text = prize,
            modifier = Modifier.fillMaxWidth(),
            style = TextStyle(
                fontSize = 18.sp,
                fontFamily = lexendFontFamily,
                fontWeight = FontWeight.SemiBold,
                color = BlueDark)
        )
        
        Text(
            text = date,
            modifier = Modifier.fillMaxWidth(),
            style = TextStyle(
                fontSize = 18.sp,
                fontFamily = lexendFontFamily,
                fontWeight = FontWeight.Normal,
                color = BlueDark)
        )
    }
}

@Preview
@Composable
fun GridItemPreview(){
    GridItem(tournament="Tournament", prize="Prize", date="Date", onClick = {})
}

@Composable
fun TournamentList(tournaments: List<Tournament>) {
    LazyColumn {
        items(tournaments) { tournament ->
            Text(text = "${tournament.name} - ${tournament.location}")
            Text(text = "${tournament.date} - ${tournament.prize}")
            AsyncImage(model = tournament.imageUrl, contentDescription = null)
        }
    }
}
