package grad.project.padelytics.features.about.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import grad.project.padelytics.features.about.data.InfoPage
import grad.project.padelytics.ui.theme.Blue
import grad.project.padelytics.ui.theme.BlueDark
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

@Composable
fun SmallGreenButton(label: String, onClick: () -> Unit ){
    Button(onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(containerColor = GreenLight),
        modifier = Modifier.width(160.dp).height(60.dp),
        shape = RoundedCornerShape(100.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    )
    {
        Text(label,
            fontSize = 20.sp,
            color = BlueDark ,
            fontFamily = lexendFontFamily,
            fontWeight = FontWeight.SemiBold)
    }
}