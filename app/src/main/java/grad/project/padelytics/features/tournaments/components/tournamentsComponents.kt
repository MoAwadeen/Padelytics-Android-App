package grad.project.padelytics.features.tournaments.components

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import grad.project.padelytics.R
import grad.project.padelytics.appComponents.WideGreenButton
import grad.project.padelytics.features.tournaments.data.Tournament
import grad.project.padelytics.features.tournaments.viewModel.TournamentsViewModel
import grad.project.padelytics.navigation.Routes
import grad.project.padelytics.ui.theme.Blue
import grad.project.padelytics.ui.theme.BlueDark
import grad.project.padelytics.ui.theme.GreenDark
import grad.project.padelytics.ui.theme.GreenLight
import grad.project.padelytics.ui.theme.WhiteGray
import grad.project.padelytics.ui.theme.lexendFontFamily

@Composable
fun GridItem(tournament: String, prize: String, date: String, imageUrl: String, onClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {

        Box(
            modifier = Modifier
                .width(192.dp)
                .height(242.dp)
                .clip(RoundedCornerShape(12.dp))
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
                    .width(190.dp)
                    .height(240.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onClick() },
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .scale(Scale.FILL)
                    .build(),
                contentDescription = "Tournament Image",
                contentScale = ContentScale.Crop
            )
        }

        Text(
            text = tournament,
            modifier = Modifier.fillMaxWidth(),
            style = TextStyle(
                fontSize = 18.sp,
                fontFamily = lexendFontFamily,
                fontWeight = FontWeight.SemiBold,
                color = Blue
            ),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = prize,
            modifier = Modifier.fillMaxWidth(),
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = lexendFontFamily,
                fontWeight = FontWeight.SemiBold,
                color = BlueDark
            )
        )

        Text(
            text = date,
            modifier = Modifier.fillMaxWidth(),
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = lexendFontFamily,
                fontWeight = FontWeight.Normal,
                color = BlueDark
            )
        )
    }
}

@Preview
@Composable
fun GridItemPreview(){
    Surface (
        modifier = Modifier.fillMaxSize().background(White),
    ){
        GridItem(tournament="Tournament", prize="Prize", date="Date", imageUrl = "imageUrl", onClick = {})
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TournamentAppToolbar(navController: NavController, tournamentName: String) {
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
                    onClick = {
                        navController.navigate(Routes.TOURNAMENTS)
                    },
                    modifier = Modifier.size(40.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.back) ,
                        contentDescription = "Back",
                        modifier = Modifier.size(32.dp)
                    )
                }
                Text(
                    text = tournamentName,
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
fun TournamentAppToolbarPreview(){
    TournamentAppToolbar(navController = rememberNavController(), tournamentName = "Tournament Name")
}

@Composable
fun TournamentDetails(tournament: Tournament, viewModel: TournamentsViewModel = viewModel()) {
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
                Text(text = "Are you sure you want to remove this tournament from your favorites?",
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
                        viewModel.removeFavoriteTournament(tournament.id) { success ->
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

    LaunchedEffect(tournament.id) {
        viewModel.checkIfFavorite(tournament.id) { result ->
            isFavorite = result
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(262.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .width(202.dp)
                        .height(262.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .shadow(
                            elevation = 4.dp,
                            shape = RoundedCornerShape(20.dp),
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
                            .width(200.dp)
                            .height(260.dp)
                            .clip(RoundedCornerShape(20.dp)),
                        model = tournament.image,
                        contentDescription = "Tournament Image",
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
                        .shadow(
                            elevation = 0.5.dp,
                            shape = RoundedCornerShape(10.dp),
                            spotColor = Color.Gray.copy(alpha = 0.001f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = {
                            if (isFavorite) {
                                showDialog = true
                            } else {
                                viewModel.saveFavoriteTournament(tournament) { success ->
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

        Text(
            text = tournament.tournamentName,
            modifier = Modifier.fillMaxWidth(),
            style = TextStyle(
                fontSize = 18.sp,
                fontFamily = lexendFontFamily,
                fontWeight = FontWeight.Medium,
                color = BlueDark
            )
        )

        Text(
            text = "Prizes: EGP ${tournament.prize}",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp, bottom = 15.dp),
            style = TextStyle(
                fontSize = 22.sp,
                fontFamily = lexendFontFamily,
                fontWeight = FontWeight.Normal,
                color = Blue
            )
        )

        Text(
            text = "Registration Fees: EGP ${tournament.registrationFees}",
            modifier = Modifier.fillMaxWidth(),
            style = TextStyle(
                fontSize = 20.sp,
                fontFamily = lexendFontFamily,
                fontWeight = FontWeight.Normal,
                color = GreenDark
            )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp, bottom = 40.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .width(30.dp)
                    .height(30.dp)
                    .padding(end = 5.dp),
                painter = painterResource(R.drawable.location),
                contentDescription = "Location",
                tint = Blue
            )

            Text(
                text = tournament.location,
                modifier = Modifier.fillMaxWidth(),
                style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = lexendFontFamily,
                    fontWeight = FontWeight.Medium,
                    color = Blue
                )
            )
        }

        WideGreenButton(
            label = "Contact Now",
            onClick = {
                tournament.url.let { url ->
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        Log.e("TournamentDetails", "Error opening URL: $url", e)
                        Toast.makeText(context, "Failed to open link", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        )
    }
}

@Preview
@Composable
fun TournamentDetailsPreview(){
    Surface (
        modifier = Modifier.fillMaxSize().background(White),
    ){
        TournamentDetails(tournament = Tournament())
    }
}