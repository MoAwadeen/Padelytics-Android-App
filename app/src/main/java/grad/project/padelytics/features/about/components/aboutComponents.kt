package grad.project.padelytics.features.about.components

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import grad.project.padelytics.R
import grad.project.padelytics.features.about.data.InfoPage
import grad.project.padelytics.features.about.data.TeamInfo
import grad.project.padelytics.features.auth.components.SmallGreenButton
import grad.project.padelytics.ui.theme.Blue
import grad.project.padelytics.ui.theme.BlueDark
import grad.project.padelytics.ui.theme.GreenDark
import grad.project.padelytics.ui.theme.GreenLight
import grad.project.padelytics.ui.theme.lexendFontFamily
import kotlinx.coroutines.launch

@Composable
fun InfoScroller(pages: List<InfoPage>, onFinished: () -> Unit) {
    val pagerState = rememberPagerState(initialPage = 0) { pages.size }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Blue),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            userScrollEnabled = false,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            val info = pages[page]

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = info.image,
                    contentDescription = "Page $page image",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(pages.size) { index ->
                        val isSelected = pagerState.currentPage == index
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) GreenLight else Blue)
                                .border(width = 1.dp, color = BlueDark, shape = CircleShape)
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = info.description,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = lexendFontFamily,
                        fontWeight = FontWeight.Medium,
                        color = White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(horizontal = 30.dp),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (page == pages.lastIndex) {
                        SmallGreenButton(
                            label = "Back",
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(page= page - 1)
                                }
                            }
                        )
                        SmallGreenButton(
                            label = "Finish",
                            onClick = { onFinished() }
                        )
                    } else {
                        if (page == 0) {
                            SmallGreenButton(
                                label = "Back",
                                onClick = { onFinished() }
                            )
                            SmallGreenButton(
                                label = "Next",
                                onClick = {
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(page= 1)
                                    }
                                }
                            )
                        }

                        if (page > 0) {
                            SmallGreenButton(
                                label = "Back",
                                onClick = {
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(page= page - 1)
                                    }
                                }
                            )
                            SmallGreenButton(
                                label = "Next",
                                onClick = {
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(page = page + 1)
                                    }
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutUsAppToolbar() {
    TopAppBar(
        modifier = Modifier.fillMaxWidth().height(80.dp),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Blue,
            titleContentColor = White,
        ),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.padelytics_2),
                    contentDescription = "Logo",
                    modifier = Modifier.width(160.dp).height(36.dp)
                )
            }
        }
    )
}

@Composable
fun Titles(title: String){
    Row(modifier = Modifier.fillMaxWidth()){
        Text(
            text = title,
            style = TextStyle(
                fontSize = 22.sp,
                fontFamily = lexendFontFamily,
                fontWeight = FontWeight.Bold,
                color = BlueDark
            )
        )
        Text(
            text = ".",
            style = TextStyle(
                fontSize = 22.sp,
                fontFamily = lexendFontFamily,
                fontWeight = FontWeight.Bold,
                color = GreenLight
            )
        )
    }
}

@Composable
fun Description(description: String){
    Text(
        text = description,
        style = TextStyle(
            fontSize = 16.sp,
            fontFamily = lexendFontFamily,
            fontWeight = FontWeight.Medium,
            color = Blue
        )
    )
}

@Composable
fun TitlesHeaders(title: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            thickness = 2.dp,
            color = GreenLight
        )

        Text(
            text = title,
            style = TextStyle(
                fontSize = 20.sp,
                fontFamily = lexendFontFamily,
                fontWeight = FontWeight.Normal,
                color = GreenDark
            ),
            modifier = Modifier.padding(horizontal = 8.dp),
            textAlign = TextAlign.Center
        )

        HorizontalDivider(
            modifier = Modifier.weight(1f),
            thickness = 2.dp,
            color = GreenLight
        )
    }
}

@Composable
fun MemberCard(member: TeamInfo, context: Context) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                try {
                    val intent = Intent(Intent.ACTION_VIEW, member.memberLinkedIn.toUri())
                    context.startActivity(intent)
                } catch (e: Exception) {
                    Log.e("AboutTeam", "Error opening URL: ${member.memberLinkedIn}", e)
                    Toast.makeText(context, "Failed to open link", Toast.LENGTH_SHORT).show()
                }
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(White)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(member.memberPhoto)
                    .crossfade(true)
                    .scale(Scale.FILL)
                    .build(),
                contentDescription = "Member Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .border(4.dp, Blue, CircleShape)
                    .clip(CircleShape)
                    .background(White)
            )
        }

        Text(
            text = member.memberName,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = lexendFontFamily,
                fontWeight = FontWeight.SemiBold,
                color = Blue
            ),
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = member.memberTitle,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = lexendFontFamily,
                fontWeight = FontWeight.Normal,
                color = GreenDark
            ),
            textAlign = TextAlign.Center
        )
    }
}


@Composable
fun MemberInfoGrid(teamInfo: List<TeamInfo>) {
    val context = LocalContext.current

    if (teamInfo.isEmpty()) return

    val isGrid = teamInfo.size > 1

    if (isGrid) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(count = 2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 500.dp)
        ) {
            items(teamInfo.size) { index ->
                MemberCard(teamInfo[index], context)
            }
        }
    } else {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            MemberCard(teamInfo.first(), context)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AboutUsAppToolbarPreview() {
    AboutUsAppToolbar()
}

@Preview(showBackground = true)
@Composable
fun InfoScrollerPreview(){
    MemberInfoGrid(teamInfo = listOf(TeamInfo(
        memberName = "Merna Hesham",
        memberTitle = "Team Leader\nAndroid developer",
        memberPhoto = R.drawable.merna,
        memberLinkedIn = ""
    ),
        TeamInfo(
            memberName = "Mohamed Awadeen",
            memberTitle = "Android developer\nUi/Ux designer",
            memberPhoto = null,
            memberLinkedIn = ""
        )))
}