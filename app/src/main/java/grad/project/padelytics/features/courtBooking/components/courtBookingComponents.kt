package grad.project.padelytics.features.courtBooking.components

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import grad.project.padelytics.R
import grad.project.padelytics.appComponents.WideGreenButton
import grad.project.padelytics.features.courtBooking.data.Court
import grad.project.padelytics.features.courtBooking.viewModel.CourtBookingViewModel
import grad.project.padelytics.ui.theme.Blue
import grad.project.padelytics.ui.theme.BlueDark
import grad.project.padelytics.ui.theme.GreenDark
import grad.project.padelytics.ui.theme.GreenLight
import grad.project.padelytics.ui.theme.WhiteGray
import grad.project.padelytics.ui.theme.lexendFontFamily
import kotlin.math.roundToInt

@Composable
fun CourtHeaders(
    selectedPlayers: Int,
    onPlayersChange: (Int) -> Unit,
    selectedCity: String,
    onCityChange: (String) -> Unit,
    cities: List<String>
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(color = White),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Players",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontFamily = lexendFontFamily,
                    fontWeight = FontWeight.Medium,
                    color = BlueDark
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            CustomSwipeToggleSwitch(selectedValue = selectedPlayers, onValueChange = onPlayersChange)
        }

        Column(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Location",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontFamily = lexendFontFamily,
                    fontWeight = FontWeight.Medium,
                    color = BlueDark
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Box {
                Row(
                    modifier = Modifier
                        .width(110.dp)
                        .height(40.dp)
                        .clickable { expanded = true },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        modifier = Modifier
                            .width(30.dp)
                            .height(30.dp)
                            .padding(end = 4.dp),
                        painter = painterResource(R.drawable.location),
                        contentDescription = "Location",
                        tint = BlueDark
                    )

                    Text(
                        text = selectedCity,
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = lexendFontFamily,
                            fontWeight = FontWeight.Light,
                            color = BlueDark
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                DropdownMenu(
                    modifier = Modifier.background(color = White),
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    cities.forEach { city ->
                        DropdownMenuItem(
                            modifier = Modifier.background(White),
                            text = { Text(text = city,
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontFamily = lexendFontFamily,
                                    fontWeight = FontWeight.Light,
                                    color = BlueDark
                                ))
                                   },
                            onClick = {
                                onCityChange(city)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CourtHeadersPreview(){
    var selectedValue by remember { mutableIntStateOf(2) }
    CourtHeaders(selectedPlayers = selectedValue,
        onPlayersChange = { selectedValue = it },
        selectedCity = "City",
        onCityChange = {},
        cities = listOf()
    )
}

@Composable
fun CustomSwipeToggleSwitch(
    selectedValue: Int,
    onValueChange: (Int) -> Unit
) {
    val toggleWidth = 90.dp
    val toggleHeight = 40.dp
    val thumbWidth = toggleWidth / 2

    val density = LocalDensity.current
    val maxOffsetPx = with(density) { thumbWidth.toPx() }

    var offsetX by remember { mutableFloatStateOf(if (selectedValue == 2) 0f else maxOffsetPx) }

    val animatedOffset by animateFloatAsState(
        targetValue = offsetX,
        animationSpec = tween(250),
        label = "SwipeToggleOffset"
    )

    Box(
        modifier = Modifier
            .width(toggleWidth)
            .height(toggleHeight)
            .clip(RoundedCornerShape(30.dp))
            .background(White)
            .border(3.dp, GreenLight, RoundedCornerShape(30.dp))
            .pointerInput(Unit) {
                detectTapGestures { tapOffset ->
                    val newValue = if (tapOffset.x < size.width / 2f) 2 else 4
                    offsetX = if (newValue == 2) 0f else maxOffsetPx
                    onValueChange(newValue)
                }
            }
    ) {
        Box(
            modifier = Modifier
                .offset { IntOffset(animatedOffset.roundToInt(), 0) }
                .width(thumbWidth)
                .fillMaxHeight()
                .clip(RoundedCornerShape(30.dp))
                .background(GreenLight)
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        offsetX = (offsetX + delta).coerceIn(0f, maxOffsetPx)
                    },
                    onDragStopped = {
                        val newValue = if (offsetX < maxOffsetPx / 2) 2 else 4
                        offsetX = if (newValue == 2) 0f else maxOffsetPx
                        onValueChange(newValue)
                    }
                )
        )

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "2",
                color = if (selectedValue == 2) BlueDark else GreenDark,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                fontFamily = lexendFontFamily
            )
            Text(
                text = "4",
                color = if (selectedValue == 4) BlueDark else GreenDark,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                fontFamily = lexendFontFamily
            )
        }
    }
}

@Preview
@Composable
fun CustomSwipeToggleSwitchPreview() {
    CustomSwipeToggleSwitch(selectedValue = 2, onValueChange = {})
}

@Composable
fun CourtItem(
    viewModel: CourtBookingViewModel,
    court: Court ,
    courtId: String,
    courtImage: String,
    courtName: String,
    courtRating: String,
    courtNumRating: String,
    courtPrice: String,
    onClick: () -> Unit
) {
    var isFavorite by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var showRemoveDialog by remember { mutableStateOf(false) }

    if (showRemoveDialog) {
        AlertDialog(
            containerColor = Blue,
            titleContentColor = BlueDark,
            textContentColor = GreenLight,
            onDismissRequest = { showRemoveDialog = false },
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
                        showRemoveDialog = false
                        viewModel.removeFavoriteCourt(courtId){ success ->
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
                    onClick = { showRemoveDialog = false }
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

    LaunchedEffect(courtId) {
        viewModel.checkIfFavorite(courtId) { result ->
            isFavorite = result
        }
    }

    val ratingValue = courtRating.toFloatOrNull() ?: 0f
    val fullStars = ratingValue.toInt()
    val decimalPart = ratingValue - fullStars

    val extraStar: Int? = when {
        decimalPart < 0.3f -> null
        decimalPart < 0.8f -> R.drawable.half_rating
        else -> R.drawable.rating
    }

    val starImages = List(5) { index ->
        when {
            index < fullStars -> R.drawable.rating
            index == fullStars && extraStar != null -> extraStar
            else -> R.drawable.no_rating
        }
    }

    Box(modifier = Modifier.fillMaxWidth().height(110.dp)) {
        Row(modifier = Modifier.fillMaxWidth()
            .clickable { onClick() },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .width(110.dp)
                    .height(110.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .shadow(
                        elevation = 1.dp,
                        shape = RoundedCornerShape(12.dp),
                        spotColor = Color.Gray.copy(alpha = 0.005f)
                    )
                    .drawBehind {
                        drawRect(
                            color = Color.Gray.copy(alpha = 0.05f),
                            topLeft = Offset(2.dp.toPx(), size.height - 8.dp.toPx()),
                            size = Size(size.width - 6.dp.toPx(), 8.dp.toPx())
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    modifier = Modifier
                        .width(108.dp)
                        .height(108.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(color = White),
                    model = courtImage,
                    contentDescription = "Court Image",
                    contentScale = ContentScale.Fit,
                    alignment = Alignment.Center
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            Column(
                modifier = Modifier.weight(1f).fillMaxHeight().padding(start = 2.dp, top = 4.dp, bottom = 4.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = courtName,
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

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = courtRating,
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = lexendFontFamily,
                            fontWeight = FontWeight.Medium,
                            color = BlueDark
                        )
                    )

                    starImages.forEach { starImage ->
                        Image(
                            painter = painterResource(starImage),
                            contentDescription = "Star Rating",
                            modifier = Modifier.size(14.dp)
                        )
                    }

                    val ratingNum = if (courtNumRating == "") "" else "($courtNumRating)"

                    Text(
                        text = ratingNum,
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = lexendFontFamily,
                            fontWeight = FontWeight.Medium,
                            color = BlueDark
                        )
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = courtPrice,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = lexendFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = BlueDark
                    )
                )
            }

            Column(
                modifier = Modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Top
            ) {
                Box(
                    modifier = Modifier
                        .width(28.dp)
                        .height(28.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = {
                            if (isFavorite) {
                                showRemoveDialog = true
                            } else {
                                viewModel.saveFavoriteCourt(court) { success ->
                                    if (success) {
                                        isFavorite = true
                                        Toast.makeText(context, "Added to favorites", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(context, "Failed to add", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Image(
                            painter = painterResource(
                                if (isFavorite) R.drawable.fav_selected else R.drawable.fav_unselected
                            ),
                            contentDescription = "Favorite",
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CourtItemPreview(){
    CourtItem(viewModel = CourtBookingViewModel(),
        court = Court(),
        courtId = "1",
        courtImage = "",
        courtName = "Court Name",
        courtRating = "4.5",
        courtNumRating = "200",
        courtPrice = "EGP 300/h",
        onClick = {})
}

@Composable
fun CourtDetails(court: Court, viewModel: CourtBookingViewModel = viewModel()) {
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
                        viewModel.removeFavoriteCourt(court.courtId) { success ->
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
        viewModel.checkIfFavorite(court.courtId) { result ->
            isFavorite = result
        }
    }

    val ratingValue = court.courtRating.toFloatOrNull() ?: 0f
    val fullStars = ratingValue.toInt()
    val decimalPart = ratingValue - fullStars

    val extraStar: Int? = when {
        decimalPart < 0.3f -> null
        decimalPart < 0.8f -> R.drawable.half_rating
        else -> R.drawable.rating
    }

    val starImages = List(5) { index ->
        when {
            index < fullStars -> R.drawable.rating
            index == fullStars && extraStar != null -> extraStar
            else -> R.drawable.no_rating
        }
    }

    val pagerState = rememberPagerState(pageCount = { 2 })

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .height(280.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                ) {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxSize()
                    ) { page ->
                        val imageUrl = if (page == 0) court.firstPhoto else court.secondPhoto
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = "Court Image $page",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 18.dp, end = 14.dp)
                            .width(40.dp)
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(modifier = Modifier.width(40.dp).height(200.dp)){
                            Box(modifier = Modifier
                                .width(32.dp)
                                .height(32.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(color = White)
                                .shadow(
                                    elevation = 0.5.dp,
                                    shape = RoundedCornerShape(10.dp),
                                    spotColor = Color.Gray.copy(alpha = 0.001f)
                                ),
                                contentAlignment = Alignment.Center){
                                IconButton(
                                    onClick = {
                                        if (isFavorite) {
                                            showDialog = true
                                        } else {
                                            viewModel.saveFavoriteCourt(court) { success ->
                                                if (success) {
                                                    isFavorite = true
                                                    Toast.makeText(context, "Added to favorites", Toast.LENGTH_SHORT).show()
                                                } else {
                                                    Toast.makeText(context, "Failed to add", Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                        }
                                    },
                                    modifier = Modifier.size(30.dp).background(color = White)
                                ) {
                                    Image(
                                        painter = painterResource(
                                            if (isFavorite) R.drawable.fav_selected else R.drawable.fav_unselected
                                        ),
                                        contentDescription = "Favorite",
                                        modifier = Modifier.size(24.dp).background(color = White)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Box(modifier = Modifier
                                .width(32.dp)
                                .height(32.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(color = White)
                                .shadow(
                                    elevation = 0.5.dp,
                                    shape = RoundedCornerShape(10.dp),
                                    spotColor = Color.Gray.copy(alpha = 0.001f)
                                ),
                                contentAlignment = Alignment.CenterEnd){
                                IconButton(
                                    onClick = {
                                        court.courtOnMap.let { url ->
                                            try {
                                                val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                                                context.startActivity(intent)
                                            } catch (e: Exception) {
                                                Log.e("CourtDetails", "Error opening URL: $url", e)
                                                Toast.makeText(context, "Failed to open link", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    },
                                    modifier = Modifier.size(30.dp).background(color = White)
                                ) {
                                    Image(
                                        painter = painterResource(R.drawable.maps_3),
                                        contentDescription = "Maps",
                                        modifier = Modifier.size(24.dp).background(color = White)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Box(modifier = Modifier
                                .width(32.dp)
                                .height(32.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(color = White)
                                .shadow(
                                    elevation = 0.5.dp,
                                    shape = RoundedCornerShape(10.dp),
                                    spotColor = Color.Gray.copy(alpha = 0.001f)
                                ),
                                contentAlignment = Alignment.Center){
                                IconButton(
                                    onClick = {
                                        court.instagramPage.let { url ->
                                            try {
                                                val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                                                context.startActivity(intent)
                                            } catch (e: Exception) {
                                                Log.e("CourtDetails", "Error opening URL: $url", e)
                                                Toast.makeText(context, "Failed to open link", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    },
                                    modifier = Modifier.size(30.dp).background(color = White)
                                ) {
                                    Image(
                                        painter = painterResource(R.drawable.instagram_2),
                                        contentDescription = "Instagram",
                                        modifier = Modifier.size(26.dp).background(color = White)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(2) { index ->
                        val isSelected = pagerState.currentPage == index
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) LightGray else White)
                                .border(width = 1.dp, color = LightGray, shape = CircleShape)
                        )
                    }
                }
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(start = 20.dp, end = 20.dp, bottom = 20.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp, bottom = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .width(36.dp)
                    .height(36.dp)
                    .padding(end = 5.dp),
                painter = painterResource(R.drawable.location),
                contentDescription = "Location",
                tint = BlueDark
            )

            Text(
                text = court.courtLocation,
                modifier = Modifier.fillMaxWidth(),
                style = TextStyle(
                    fontSize = 18.sp,
                    fontFamily = lexendFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = BlueDark
                )
            )
        }

        Text(
            text = "Booking Price : ${court.bookingPrice}",
            modifier = Modifier
                .fillMaxWidth(),
            style = TextStyle(
                fontSize = 18.sp,
                fontFamily = lexendFontFamily,
                fontWeight = FontWeight.SemiBold,
                color = Blue
            )
        )

        Text(
            text = "Court teams : ${court.numPlayers}",
            modifier = Modifier.fillMaxWidth()
                .padding(top = 15.dp, bottom = 15.dp),
            style = TextStyle(
                fontSize = 18.sp,
                fontFamily = lexendFontFamily,
                fontWeight = FontWeight.Medium,
                color = Blue
            )
        )

        Row(
            modifier = Modifier.padding(bottom = 40.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = court.courtRating,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontFamily = lexendFontFamily,
                    fontWeight = FontWeight.Medium,
                    color = BlueDark
                )
            )

            starImages.forEach { starImage ->
                Image(
                    painter = painterResource(starImage),
                    contentDescription = "Star Rating",
                    modifier = Modifier.size(18.dp)
                )
            }

            val ratingNum = if (court.numRating == "") "" else "(${court.numRating})"

            Text(
                text = ratingNum,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontFamily = lexendFontFamily,
                    fontWeight = FontWeight.Medium,
                    color = BlueDark
                )
            )
        }

        WideGreenButton(
            label = "Book Now",
            onClick = {
                court.bookingUrl.let { url ->
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        Log.e("CourtDetails", "Error opening URL: $url", e)
                        Toast.makeText(context, "Failed to open link", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        )
    }
}

@Preview
@Composable
fun CourtDetailsPreview(){
    Surface (
        modifier = Modifier.fillMaxSize().background(White),
    ){
        CourtDetails(court = Court())
    }
}

@Composable
fun NoCourtsAlert(){
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
                text = "No near courts",
                fontSize = 28.sp,
                fontFamily = lexendFontFamily,
                color = BlueDark,
                fontWeight = FontWeight.Bold
            )
        }
    }
}