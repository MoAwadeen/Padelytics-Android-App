package grad.project.padelytics.features.analysis.components

import android.annotation.SuppressLint
import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.withSave
import coil.compose.AsyncImage
import grad.project.padelytics.R
import grad.project.padelytics.features.analysis.data.AnimationFrame
import grad.project.padelytics.features.analysis.data.BallSpeedOverTime
import grad.project.padelytics.features.analysis.data.BallTrajectory
import grad.project.padelytics.features.analysis.data.FullAnalysisData
import grad.project.padelytics.features.analysis.data.PlayerHeatmap
import grad.project.padelytics.features.analysis.data.PlayerHitLocations
import grad.project.padelytics.features.analysis.data.RadarPerformance
import grad.project.padelytics.features.analysis.data.StrongestHitItem
import grad.project.padelytics.features.analysis.data.TrajectoryData
import grad.project.padelytics.features.results.data.PlayerInfo
import grad.project.padelytics.ui.theme.Blue
import grad.project.padelytics.ui.theme.BlueDark
import grad.project.padelytics.ui.theme.GreenDark
import grad.project.padelytics.ui.theme.GreenLight
import grad.project.padelytics.ui.theme.OrangeLight
import grad.project.padelytics.ui.theme.lexendFontFamily
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun AnalysisAppbar() {
    Row(modifier = Modifier.fillMaxWidth()
        .height(50.dp)
        .background(Blue)
        .padding(top = 6.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = "Match Results",
            style = TextStyle(
                fontSize = 32.sp,
                fontFamily = lexendFontFamily,
                fontWeight = FontWeight.Medium,
                color = White
            )
        )
    }
}

@Composable
fun PlayersView(players: List<PlayerInfo>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Blue)
            .padding(horizontal = 16.dp)
            .height(100.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        players.forEachIndexed { index, player ->
            if (index == 2) {
                Spacer(modifier = Modifier.weight(1f))
                Column(
                    modifier = Modifier.fillMaxHeight().wrapContentWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "VS",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = lexendFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = White
                        )
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
            }

            Column(
                modifier = Modifier.fillMaxHeight().wrapContentWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .shadow(8.dp, CircleShape)
                        .background(Transparent),
                    contentAlignment = Alignment.Center
                ) {
                    if (player.photo.isNotBlank()) {
                        AsyncImage(
                            model = player.photo,
                            contentDescription = "Avatar",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .border(2.dp, if (index < 2) GreenLight else BlueDark, CircleShape)
                                .clip(CircleShape)
                                .background(Transparent)
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.user_selected),
                            contentDescription = "Default Avatar",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .fillMaxSize()
                                .border(2.dp, if (index < 2) GreenLight else BlueDark, CircleShape)
                                .clip(CircleShape)
                                .background(Transparent)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = player.firstName,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = lexendFontFamily,
                        fontWeight = FontWeight.Medium,
                        color = White
                    )
                )
            }

            if (index != 1 && index != 3) {
                Spacer(modifier = Modifier.weight(1f))
            }
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

@Composable
fun BallAnalysisBox(graphScreens: List<Pair<String, @Composable () -> Unit>>) {
    var currentIndex by remember { mutableIntStateOf(value = 0) }
    val (currentTitle, currentCourtComposable) = graphScreens[currentIndex]

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Blue)
    ) {
        Image(
            painter = painterResource(R.drawable.ball_analysis_bg),
            contentDescription = "Ball Analysis Background",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .align(Alignment.Center)
                .padding(horizontal = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.height(180.dp)
                    .clickable {
                    currentIndex = if (currentIndex == 0) graphScreens.lastIndex else currentIndex - 1
                },
                verticalArrangement = Arrangement.Center
            ){
                Image(
                    painter = painterResource(R.drawable.left_arrow),
                    contentDescription = "Left Arrow",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(30.dp)
                        .clickable {
                            currentIndex = if (currentIndex == 0) graphScreens.lastIndex else currentIndex - 1
                        }
                )
            }

            currentCourtComposable()

            Column(
                modifier = Modifier.height(180.dp)
                    .clickable {
                    currentIndex = (currentIndex + 1) % graphScreens.size
                },
                verticalArrangement = Arrangement.Center
            ){
                Image(
                    painter = painterResource(R.drawable.right_arrow),
                    contentDescription = "Right Arrow",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(30.dp)
                        .clickable {
                            currentIndex = (currentIndex + 1) % graphScreens.size
                        }
                )
            }
        }

        Text(
            text = currentTitle,
            style = TextStyle(
                fontSize = 20.sp,
                fontFamily = lexendFontFamily,
                fontWeight = FontWeight.Bold,
                color = White,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp)
        )
    }
}

@Composable
fun CourtBackground(content: @Composable BoxScope.() -> Unit = {}) {
    Box(
        modifier = Modifier
            .width(230.dp)
            .height(100.dp)
            .background(Blue),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier.matchParentSize()
        ) {
            val borderColor = BlueDark
            val lineColor = BlueDark
            val borderStroke = 4.dp.toPx()
            val lineStroke = 4.dp.toPx()

            val totalWidth = size.width
            val totalHeight = size.height

            // x is the padding from the edge to the first/third vertical lines
            val x = totalWidth / 8f

            val v1 = x                          // First vertical line
            val v2 = totalWidth / 2f            // Middle of the court (second vertical line)
            val v3 = totalWidth - x             // Third vertical line

            val horizontalY = totalHeight / 2f  // Half the height

            // Horizontal line: from center of v1 to center of v3
            drawLine(
                color = lineColor,
                start = Offset(x = v1, y = horizontalY),
                end = Offset(x = v3, y = horizontalY),
                strokeWidth = lineStroke
            )

            // Vertical lines
            listOf(v1, v2, v3).forEach { xPos ->
                drawLine(
                    color = lineColor,
                    start = Offset(x = xPos, y = 0f),
                    end = Offset(x = xPos, y = totalHeight),
                    strokeWidth = lineStroke
                )
            }

            // Outer border
            drawRoundRect(
                color = borderColor,
                topLeft = Offset.Zero,
                size = size,
                cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx()),
                style = Stroke(width = borderStroke)
            )
        }
        content()
    }
}

@Composable
fun RectangleBackground(content: @Composable BoxScope.() -> Unit = {}) {
    Box(
        modifier = Modifier
            .width(230.dp)
            .height(100.dp)
            .background(Blue),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier.matchParentSize()
        ) {
            val borderColor = BlueDark
            val borderStroke = 4.dp.toPx()

            drawRoundRect(
                color = borderColor,
                topLeft = Offset.Zero,
                size = size,
                cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx()),
                style = Stroke(width = borderStroke)
            )
        }
        content()
    }
}

@Composable
fun BallTrajectoryPlot(ballTrajectory: BallTrajectory) {
    if (ballTrajectory.x.isEmpty() || ballTrajectory.y.isEmpty()) {
        Text("No trajectory data", color = Color.White)
        return
    }

    // Fixed coordinate space: -10 to 10
    val fixedXMin = -10f
    val fixedXMax = 10f
    val fixedYMin = -10f
    val fixedYMax = 10f
    val rangeX = fixedXMax - fixedXMin
    val rangeY = fixedYMax - fixedYMin

    // Normalize raw ball data to fixed -10 to 10 space
    val ballMinX = ballTrajectory.x.minOrNull() ?: fixedXMin
    val ballMaxX = ballTrajectory.x.maxOrNull() ?: fixedXMax
    val ballMinY = ballTrajectory.y.minOrNull() ?: fixedYMin
    val ballMaxY = ballTrajectory.y.maxOrNull() ?: fixedYMax

    fun normalize(value: Float, min: Float, max: Float): Float =
        if (max - min == 0f) 0f else (value - min) / (max - min) * 20f - 10f

    val normalizedPoints = ballTrajectory.x.zip(ballTrajectory.y).map { (x, y) ->
        val normX = normalize(x, ballMinX, ballMaxX)
        val normY = normalize(y, ballMinY, ballMaxY)
        normX to normY
    }

    Canvas(
        modifier = Modifier
            .width(230.dp)
            .height(100.dp)
            .padding(4.dp)
            .background(Color.Transparent)
    ) {
        rotate(degrees = 180f, pivot = Offset(size.width / 2, size.height / 2)) {
            val width = size.width
            val height = size.height

            // Draw connected lines using mapped normalized coordinates
            for (i in 0 until normalizedPoints.size - 1) {
                val (startX, startY) = normalizedPoints[i]
                val (endX, endY) = normalizedPoints[i + 1]

                val canvasStartX = ((-startY - fixedXMin) / rangeX) * width
                val canvasStartY = ((-startX - fixedYMin) / rangeY) * height
                val canvasEndX = ((-endY - fixedXMin) / rangeX) * width
                val canvasEndY = ((-endX - fixedYMin) / rangeY) * height

                drawLine(
                    color = GreenLight,
                    start = Offset(canvasStartX, canvasStartY),
                    end = Offset(canvasEndX, canvasEndY),
                    strokeWidth = 3f
                )
            }
        }
    }
}

@Composable
fun BallSpeedOverTimeLineChart(data: BallSpeedOverTime) {
    val frameData = data.frame
    val speedData = data.speed

    if (frameData.isEmpty() || speedData.isEmpty() || frameData.size != speedData.size) {
        Text("No speed data available")
        return
    }

    Canvas(
        modifier = Modifier
            .width(230.dp)
            .height(100.dp)
            .padding(4.dp)
            .background(Transparent)
    ) {
        val width = size.width
        val height = size.height

        val minX = frameData.minOrNull() ?: 0f
        val maxX = frameData.maxOrNull() ?: 1f
        val minY = speedData.minOrNull() ?: 0f
        val maxY = speedData.maxOrNull() ?: 1f

        val rangeX = (maxX - minX).takeIf { it != 0f } ?: 1f
        val rangeY = (maxY - minY).takeIf { it != 0f } ?: 1f

        for (i in 1 until frameData.size) {
            val x1 = ((frameData[i - 1] - minX) / rangeX) * width
            val y1 = height - ((speedData[i - 1] - minY) / rangeY) * height

            val x2 = ((frameData[i] - minX) / rangeX) * width
            val y2 = height - ((speedData[i] - minY) / rangeY) * height

            drawLine(
                color = GreenLight,
                start = Offset(x1, y1),
                end = Offset(x2, y2),
                strokeWidth = 1.dp.toPx()
            )
        }
    }
}

@Composable
fun TopStrongestHitsBarChart(topHits: List<StrongestHitItem>, playerName: Map<String, String?> = emptyMap()) {
    if (topHits.isEmpty()) {
        Text("No data available", color = White)
        return
    }

    val sortedHits = topHits.sortedByDescending { it.speed }
    val barColors = GreenLight

    Canvas(
        modifier = Modifier
            .width(230.dp)
            .height(100.dp)
            .padding(4.dp)
            .background(Transparent)
    ) {
        val width = size.width
        val height = size.height

        val barCount = sortedHits.size
        val spacing = width * 0.1f / barCount
        val barAreaWidth = width - spacing * (barCount + 1)

        val maxSpeed = sortedHits.maxOfOrNull { it.speed } ?: 1f

        val labelPaint = Paint().apply {
            color = android.graphics.Color.WHITE
            textSize = 34f
            textAlign = Paint.Align.CENTER
            isFakeBoldText = true
        }

        val rankPaint = Paint().apply {
            color = 0xFF163300.toInt()
            textSize = 44f
            textAlign = Paint.Align.CENTER
            isFakeBoldText = true
        }

        sortedHits.forEachIndexed { index, hit ->
            val normalizedHeight = (hit.speed / maxSpeed)
            val barHeight = normalizedHeight * height * 0.7f

            val widthFactor = 0.6f + (1f - normalizedHeight) * 0.3f
            val barWidth = (barAreaWidth / barCount) * widthFactor

            val left = spacing + index * ((barAreaWidth / barCount) + spacing)
            val top = height - barHeight
            val centerX = left + barWidth / 2

            drawRoundRect(
                color = barColors,
                topLeft = Offset(left, top),
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(x = 4.dp.toPx(), y = 4.dp.toPx())
            )

            val rank = index + 1
            drawContext.canvas.nativeCanvas.drawText(
                "$rank",
                centerX,
                top + barHeight / 2 + 12f,
                rankPaint
            )

            drawContext.canvas.nativeCanvas.drawText(
                "${hit.speed.toInt()} m/s",
                centerX,
                top - 16f,
                labelPaint
            )

            drawContext.canvas.nativeCanvas.drawText(
                playerName[hit.player] ?: hit.player,
                centerX,
                top - 56f,
                labelPaint
            )
        }
    }
}

@Composable
fun HitCountBarChart(hitCount: Map<String, Int>?, playerDisplayNames: Map<String, String?> = emptyMap()) {
    val hits = hitCount ?: emptyMap()
    if (hits.isEmpty()) {
        Text("No data available", color = White)
        return
    }

    val playerNames = hits.keys.map { playerDisplayNames[it] ?: it }
    val hitValues = hits.values.toList()
    val barColor = GreenLight

    Canvas(
        modifier = Modifier
            .width(230.dp)
            .height(100.dp)
            .padding(4.dp)
            .background(Transparent)
    ) {
        val labelPaint = Paint().apply {
            color = android.graphics.Color.WHITE
            textSize = 34f
            textAlign = Paint.Align.CENTER
            isFakeBoldText = true
        }

        val valuePaint = Paint().apply {
            color = 0xFF163300.toInt()
            textSize = 40f
            textAlign = Paint.Align.CENTER
            isFakeBoldText = true
        }

        val barCount = hitValues.size
        val spacing = size.width * 0.05f / barCount
        val slotWidth = (size.width - (barCount + 1) * spacing) / barCount
        val barWidth = slotWidth * 0.6f
        val maxHitCount = hitValues.maxOrNull()?.toFloat() ?: 1f

        val topTextHeight = 20.dp.toPx()

        hitValues.forEachIndexed { index, hitCount ->
            val centerX = spacing + index * (slotWidth + spacing) + slotWidth / 2

            val barHeight = (hitCount / maxHitCount) * (size.height - topTextHeight - 12.dp.toPx())
            val barTop = size.height - barHeight
            val barLeft = centerX - barWidth / 2

            drawContext.canvas.nativeCanvas.drawText(
                playerNames[index],
                centerX,
                barTop - 8f,
                labelPaint
            )

            drawRoundRect(
                color = barColor,
                topLeft = Offset(barLeft, barTop),
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
            )

            drawContext.canvas.nativeCanvas.drawText(
                hitCount.toString(),
                centerX,
                barTop + barHeight / 2 + 10f,
                valuePaint
            )
        }
    }
}

@Composable
fun BallHitLocationsPlot(ballHits: Map<String, PlayerHitLocations>) {
    val playerColors = listOf(GreenLight, GreenDark, White, Black)

    // Fixed -10 to 10 coordinate space
    val fixedXMin = -10f
    val fixedXMax = 10f
    val fixedYMin = -10f
    val fixedYMax = 10f
    val rangeX = fixedXMax - fixedXMin
    val rangeY = fixedYMax - fixedYMin

    // Get raw min/max values for normalization
    val allPoints = ballHits.flatMap { it.value.x.zip(it.value.y) }
    val rawMinX = allPoints.minOfOrNull { it.first } ?: fixedXMin
    val rawMaxX = allPoints.maxOfOrNull { it.first } ?: fixedXMax
    val rawMinY = allPoints.minOfOrNull { it.second } ?: fixedYMin
    val rawMaxY = allPoints.maxOfOrNull { it.second } ?: fixedYMax

    fun normalize(value: Float, min: Float, max: Float): Float =
        if (max - min == 0f) 0f else (value - min) / (max - min) * 20f - 10f

    val normalizedHits = ballHits.mapValues { (_, hits) ->
        hits.x.zip(hits.y).map { (x, y) ->
            val normX = normalize(x, rawMinX, rawMaxX)
            val normY = normalize(y, rawMinY, rawMaxY)
            normX to normY
        }
    }

    Canvas(
        modifier = Modifier
            .width(230.dp)
            .height(100.dp)
            .padding(4.dp)
            .background(Transparent)
    ) {
        rotate(degrees = 180f, pivot = Offset(size.width / 2, size.height / 2)) {
            val width = size.width
            val height = size.height

            normalizedHits.entries.forEachIndexed { index, (_, points) ->
                val color = playerColors.getOrElse(index) { Color.Gray }

                points.forEach { (x, y) ->
                    // Flip X: subtract from width
                    val px = width - ((-y - fixedXMin) / rangeX) * width
                    val py = ((-x - fixedYMin) / rangeY) * height

                    drawCircle(
                        color = color,
                        radius = 3.dp.toPx(),
                        center = Offset(px, py)
                    )
                }
            }
        }
    }
}

@Composable
fun MatchAnimationCard(analysisData: FullAnalysisData, playerName: Map<String, String?>){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Blue)
            .padding(horizontal = 20.dp)
            .padding(top = 10.dp, bottom = 20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().background(Blue),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = "Match Animation",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = lexendFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = GreenLight
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            CourtBackground{
                //AnimatedPlayerScatterPlot(frames = analysisData.animation, playerNameMap = playerName)

                MatchAnimationWithBall(frames = analysisData.animation, ballTrajectory = analysisData.ball_trajectory, playerNameMap = playerName)
            }
        }
    }
}

/*
@SuppressLint("UseKtx")
@Composable
fun AnimatedPlayerScatterPlot(frames: List<AnimationFrame>, playerNameMap: Map<String, String?>) {
    if (frames.isEmpty()) {
        Text("No animation data available", color = White)
        return
    }

    val playerColors = listOf(GreenLight, GreenLight, BlueDark, BlueDark)
    val playerKeys = listOf("player1", "player2", "player3", "player4")

    var frameIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(timeMillis = 60)
            frameIndex = (frameIndex + 1) % frames.size
        }
    }

    val currentFrame = frames[frameIndex]

    Canvas(
        modifier = Modifier
            .width(230.dp)
            .height(100.dp)
            .padding(4.dp)
            .background(Transparent)
    ) {
        rotate(degrees = 180f, pivot = Offset(size.width / 2, size.height / 2)) {
            val width = size.width
            val height = size.height

            val fixedXMin = -10f
            val fixedXMax = 10f
            val rangeX = fixedXMax - fixedXMin

            val yRangeMin = -10f
            val yRangeMax = 10f
            val rangeY = yRangeMax - yRangeMin

            val positions = listOf(
                currentFrame.player1,
                currentFrame.player2,
                currentFrame.player3,
                currentFrame.player4
            )

            val labelPaint = Paint().apply {
                color = android.graphics.Color.WHITE
                textSize = 20f
                textAlign = Paint.Align.CENTER
                isFakeBoldText = true
            }

            positions.forEachIndexed { index, pos ->
                val px = ((-pos.y - fixedXMin) / rangeX) * width
                val py = ((-pos.x - yRangeMin) / rangeY) * height
                val color = playerColors.getOrElse(index) { GreenLight }
                val playerName = playerNameMap[playerKeys[index]] ?: playerKeys[index]

                drawCircle(color, radius = 3.dp.toPx(), center = Offset(px, py))

                drawContext.canvas.nativeCanvas.apply {
                    withSave {
                        rotate(-180f, px, py + 18f)
                        drawText(
                            playerName,
                            px,
                            py + 18f,
                            labelPaint
                        )
                    }
                }
            }
        }
    }
}
*/

@Composable
fun StatText(label: String, value: String) {
    Text(
        text = label,
        style = TextStyle(
            fontSize = 16.sp,
            fontFamily = lexendFontFamily,
            fontWeight = FontWeight.Medium,
            color = White)
    )

    Text(
        text = value,
        style = TextStyle(
            fontSize = 16.sp,
            fontFamily = lexendFontFamily,
            fontWeight = FontWeight.Medium,
            color = GreenLight)
    )

    Spacer(modifier = Modifier.height(10.dp))
}

@Composable
fun InsightText(title: String, desc: String, color: Color) {
    Text(
        text = title,
        style = TextStyle(
            fontSize = 18.sp,
            fontFamily = lexendFontFamily,
            fontWeight = FontWeight.Medium,
            color = White)
    )

    Text(
        text = desc,
        style = TextStyle(
            fontSize = 16.sp,
            fontFamily = lexendFontFamily,
            fontWeight = FontWeight.Medium,
            color = color)
    )

    Spacer(modifier = Modifier.height(10.dp))
}

@Composable
fun TextGraphHeader(title: String) {
    Text(
        text = title,
        style = TextStyle(
            fontSize = 18.sp,
            fontFamily = lexendFontFamily,
            fontWeight = FontWeight.Medium,
            color = White
        )
    )

    Spacer(modifier = Modifier.height(8.dp))
}

@SuppressLint("DefaultLocale")
@Composable
fun PlayerAnalysisCard(analysisData: FullAnalysisData?, playerList: List<String>, playerFirstNames: List<String>, playerPhotos: List<String>, playerLevels: List<String>) {
    if (analysisData == null || playerList.size < 4) {
        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        return
    }

    var currentPlayerIndex by remember { mutableIntStateOf(0) }
    val playerKey = playerList.getOrNull(currentPlayerIndex) ?: return
    val name = playerFirstNames.getOrNull(currentPlayerIndex) ?: "Player"
    val photo = playerPhotos.getOrNull(currentPlayerIndex).orEmpty()
    val level = playerLevels.getOrNull(currentPlayerIndex) ?: "N/A"
    val avatarBorderColors = listOf(GreenLight, GreenLight, BlueDark, BlueDark)

    val maxSpeed = analysisData.max_speed[playerKey] ?: 0.0
    val distance = analysisData.distance_total[playerKey] ?: 0.0
    val attackPresence = analysisData.zone_presence_percentages["Attack Zone"]?.get(playerKey) ?: 0.0
    val avgAcceleration = analysisData.average_acceleration[playerKey] ?: 0.0
    val totalHits = analysisData.hit_count_per_player[playerKey] ?: 0
    val defensePresence = analysisData.zone_presence_percentages["Defense Zone"]?.get(playerKey) ?: 0.0

    val reactionEfficiency = analysisData.reaction_time_efficiency[playerKey] ?: 0
    val reactionAdvice = analysisData.reaction_advice[playerKey] ?: "N/A"
    val shotEffectiveness = analysisData.shot_effectiveness[playerKey] ?: 0
    val shotAdvice = analysisData.shot_advice[playerKey] ?: "N/A"
    val role = analysisData.role[playerKey] ?: "Unknown"
    val roleAdvice = analysisData.role_advice[playerKey] ?: "No advice"
    val hitShare = analysisData.player_contribution[playerKey]?.toInt() ?: 0
    val hitAdvice = analysisData.player_contribution_advice[playerKey] ?: "No advice"
    val staminaDrop = analysisData.stamina_drop_time[playerKey] ?: 0
    val staminaAdvice = analysisData.stamina_advice[playerKey] ?: "No advice"

    val playerTrajectory = analysisData.trajectories[playerKey]
    val playerHeatmap = analysisData.heatmaps[playerKey]
    val playerAnimation = analysisData.animation
    val playerBallHits = analysisData.ball_hit_locations

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Blue)
            .padding(horizontal = 20.dp, vertical = 26.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.b_left_arrow),
                    contentDescription = "Left Arrow",
                    modifier = Modifier
                        .size(20.dp)
                        .clickable {
                            currentPlayerIndex =
                                if (currentPlayerIndex == 0) playerList.lastIndex else currentPlayerIndex - 1
                        }
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .shadow(8.dp, CircleShape)
                            .background(Transparent),
                        contentAlignment = Alignment.Center
                    ) {
                        if (photo.isNotBlank()) {
                            AsyncImage(
                                model = photo,
                                contentDescription = "Player Avatar",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .border(
                                        2.dp,
                                        avatarBorderColors.getOrElse(currentPlayerIndex) { GreenLight },
                                        CircleShape
                                    )
                                    .clip(CircleShape)
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.user_selected),
                                contentDescription = "Default Avatar",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .border(
                                        2.dp,
                                        avatarBorderColors.getOrElse(currentPlayerIndex) { GreenLight },
                                        CircleShape
                                    )
                                    .clip(CircleShape)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column(horizontalAlignment = Alignment.Start) {
                        Text(
                            text = name,
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Medium,
                                color = White
                            )
                        )
                        Text(
                            text = level,
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal,
                                color = GreenLight
                            )
                        )
                    }
                }

                Image(
                    painter = painterResource(R.drawable.b_right_arrow),
                    contentDescription = "Right Arrow",
                    modifier = Modifier
                        .size(20.dp)
                        .clickable {
                            currentPlayerIndex = (currentPlayerIndex + 1) % playerList.size
                        }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    StatText(label = "Max Speed", value = "${"%.2f".format(maxSpeed)} m/s")

                    StatText(label = "Distance", value = "${"%.2f".format(distance)} m")

                    StatText(label = "Attack", value = "${"%.2f".format(attackPresence)} %")
                }

                Spacer(modifier = Modifier.width(20.dp))

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    StatText(
                        label = "Avg Acceleration",
                        value = "${"%.2f".format(avgAcceleration)} m/s²"
                    )

                    StatText(label = "Total Hits", value = "$totalHits")

                    StatText(label = "Defense", value = "${"%.2f".format(defensePresence)} %")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            HorizontalDivider(thickness = 3.dp, color = BlueDark)

            Spacer(modifier = Modifier.height(24.dp))

            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
                InsightText(title = "Reaction Time Efficiency", desc = "$reactionEfficiency% — $reactionAdvice", color = GreenLight)

                InsightText(title = "Shot Effectiveness", desc = "$shotEffectiveness% — $shotAdvice", color = OrangeLight)

                InsightText(title = "Role Detection", desc = "$role :\n$roleAdvice", color = GreenLight)

                InsightText(title = "Team Imbalance (Hit Share)", desc = "$hitShare% — $hitAdvice", color = OrangeLight)

                InsightText(title = "Fatigue Detection Over Time", desc = "Drop in $staminaDrop minutes — $staminaAdvice", color = OrangeLight)
            }

            Spacer(modifier = Modifier.height(24.dp))

            HorizontalDivider(thickness = 3.dp, color = BlueDark)

            Spacer(modifier = Modifier.height(24.dp))

            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
                TextGraphHeader(title = "Player Trajectory")

                Row(modifier = Modifier.padding(start = 6.dp)) {
                    CourtBackground {
                        playerTrajectory?.let {
                            PlayerScatterPlot(analysisData.trajectories, playerKey)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                TextGraphHeader(title = "Positioning Heatmap")

                Row(modifier = Modifier.padding(start = 6.dp)) {
                    CourtBackground {
                        playerHeatmap?.let {
                            PlayerHeatmap(it, analysisData.heatmaps)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                TextGraphHeader(title = "Hit Points")

                Row(modifier = Modifier.padding(start = 6.dp)) {
                    CourtBackground {
                        PlayerBallHitLocationsPlot(playerBallHits, playerKey)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                TextGraphHeader(title = "Attack vs Defence")

                Row(modifier = Modifier.padding(start = 6.dp)) {
                    RectangleBackground {
                        PlayerZoneScatterGraph(playerAnimation, playerKey)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            HorizontalDivider(thickness = 3.dp, color = BlueDark)

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier.fillMaxWidth().background(Blue),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ){
                Text(
                    text = "Radar Performance",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontFamily = lexendFontFamily,
                        fontWeight = FontWeight.Medium,
                        color = White)
                )

                Spacer(modifier = Modifier.height(20.dp))

                PlayerRadarChart(analysisData.radar_performance, playerKey)
            }
        }
    }
}

@Composable
fun PlayerScatterPlot(playersData: Map<String, TrajectoryData>, targetPlayer: String) {
    val playerColor = GreenLight
    val allPoints = playersData.flatMap { (_, playerData) ->
        playerData.x.zip(playerData.y)
            .filter { it.first != null && it.second != null }
            .filter { it.first!!.isFinite() && it.second!!.isFinite() }
            .filterNot { it.first == 0f && it.second == 0f }
            .map { -it.first!! to it.second!! } // Flip X
    }

    val playerPoints = playersData[targetPlayer]?.let { data ->
        data.x.zip(data.y)
            .filter { it.first != null && it.second != null }
            .filter { it.first!!.isFinite() && it.second!!.isFinite() }
            .filterNot { it.first == 0f && it.second == 0f }
            .map { -it.first!! to it.second!! } // Flip X
    } ?: emptyList()

    if (playerPoints.isEmpty() || allPoints.isEmpty()) {
        Text("No valid points to show for $targetPlayer", color = MaterialTheme.colorScheme.error)
        return
    }

    Canvas(
        modifier = Modifier
            .width(230.dp)
            .height(100.dp)
            .padding(4.dp)
            .background(Transparent)
    ) {
        val width = size.width
        val height = size.height

        val fixedYMin = -10f
        val fixedYMax = 10f
        val rangeY = fixedYMax - fixedYMin

        val minX = allPoints.minOf { it.first }
        val maxX = allPoints.maxOf { it.first }
        val rangeX = (maxX - minX).takeIf { it != 0f } ?: 1f

        if (playerPoints.size > 1) {
            val path = Path().apply {
                val first = playerPoints.first()
                moveTo(
                    x = ((first.second - fixedYMin) / rangeY) * width,
                    y = height - ((first.first - minX) / rangeX) * height // Invert Y here
                )
                playerPoints.drop(1).forEach { (x, y) ->
                    val px = ((y - fixedYMin) / rangeY) * width
                    val py = height - ((x - minX) / rangeX) * height // Invert Y here
                    lineTo(px, py)
                }
            }
            drawPath(path, color = playerColor, style = Stroke(width = 2.dp.toPx()))
        }
    }
}

@Composable
fun PlayerHeatmap(playerHeatmap: PlayerHeatmap, allHeatmaps: Map<String, PlayerHeatmap>) {
    val pointColor = GreenLight.copy(alpha = 0.3f)

    val allPoints = allHeatmaps.flatMap { it.value.x.zip(it.value.y) }
        .filter { it.first.isFinite() && it.second.isFinite() }
        .filterNot { it.first == 0f && it.second == 0f }
        .map { -it.first to it.second }

    val playerPoints = playerHeatmap.x.zip(playerHeatmap.y)
        .filter { it.first.isFinite() && it.second.isFinite() }
        .filterNot { it.first == 0f && it.second == 0f }
        .map { -it.first to it.second }

    if (playerPoints.isEmpty() || allPoints.isEmpty()) {
        Text("No data to show for this player", color = MaterialTheme.colorScheme.error)
        return
    }

    Canvas(
        modifier = Modifier
            .width(230.dp)
            .height(100.dp)
            .padding(4.dp)
            .background(Transparent)
    ) {
        val width = size.width
        val height = size.height

        val fixedYMin = -10f
        val fixedYMax = 10f
        val rangeY = fixedYMax - fixedYMin

        val allX = allPoints.map { it.first }
        val minX = allX.minOrNull() ?: -10f
        val maxX = allX.maxOrNull() ?: 10f
        val rangeX = (maxX - minX).takeIf { it != 0f } ?: 1f

        playerPoints.forEach { (x, y) ->
            val px = ((y - fixedYMin) / rangeY) * width
            val py = height - ((x - minX) / rangeX) * height // Inverted Y

            drawCircle(
                color = pointColor,
                radius = 3.dp.toPx(),
                center = Offset(px, py)
            )
        }
    }
}

@Composable
fun PlayerBallHitLocationsPlot(ballHits: Map<String, PlayerHitLocations>, targetPlayer: String) {
    val targetHits = ballHits[targetPlayer]

    val allPoints = ballHits.flatMap { it.value.x.zip(it.value.y) }
        .filter { it.first.isFinite() && it.second.isFinite() }
        .filterNot { it.first == 0f && it.second == 0f }

    val playerPoints = targetHits?.x?.zip(targetHits.y)
        ?.filter { it.first.isFinite() && it.second.isFinite() }
        ?.filterNot { it.first == 0f && it.second == 0f }
        ?: emptyList()

    if (allPoints.isEmpty() || playerPoints.isEmpty()) {
        Text("No data to show for $targetPlayer", color = MaterialTheme.colorScheme.error)
        return
    }

    Canvas(
        modifier = Modifier
            .width(230.dp)
            .height(100.dp)
            .padding(4.dp)
            .background(Transparent)
    ) {
        rotate(degrees = 180f, pivot = Offset(size.width / 2, size.height / 2)) {
            val width = size.width
            val height = size.height

            val minX = allPoints.minOf { it.first }
            val maxX = allPoints.maxOf { it.first }
            val minY = allPoints.minOf { it.second }
            val maxY = allPoints.maxOf { it.second }

            val rangeX = (maxX - minX).takeIf { it != 0f } ?: 1f
            val rangeY = (maxY - minY).takeIf { it != 0f } ?: 1f

            playerPoints.forEach { (x, y) ->
                val px = ((y - minY) / rangeY) * width
                val py = height - ((x - minX) / rangeX) * height

                drawCircle(
                    color = GreenLight,
                    radius = 3.dp.toPx(),
                    center = Offset(px, py)
                )
            }
        }
    }
}

@Composable
fun PlayerZoneScatterGraph(frames: List<AnimationFrame>, targetPlayerKey: String) {
    if (frames.isEmpty()) {
        Text("No frame data available", color = White)
        return
    }

    val ySeries = remember(frames, targetPlayerKey) {
        frames.mapNotNull { frame ->
            val playerY = when (targetPlayerKey) {
                "player1" -> frame.player1.y
                "player2" -> frame.player2.y
                "player3" -> frame.player3.y
                "player4" -> frame.player4.y
                else -> null
            }

            if (playerY != null && playerY.isFinite()) {
                frame.Frame to playerY
            } else null
        }
    }

    if (ySeries.isEmpty()) {
        Text("No data for player", color = White)
        return
    }

    val zoneColor: (Float) -> Color = { y ->
        if (y in -5f..5f) GreenLight else OrangeLight
    }

    Canvas(
        modifier = Modifier
            .width(230.dp)
            .height(100.dp)
            .padding(4.dp)
            .background(Transparent)
    ) {
        val width = size.width
        val height = size.height

        val framesList = ySeries.map { it.first }
        val yList = ySeries.map { it.second }

        val minFrame = framesList.minOrNull() ?: 0
        val maxFrame = framesList.maxOrNull() ?: 1
        val rangeX = (maxFrame - minFrame).toFloat().takeIf { it != 0f } ?: 1f

        val minY = yList.minOrNull() ?: -10f
        val maxY = yList.maxOrNull() ?: 10f
        val rangeY = (maxY - minY).takeIf { it != 0f } ?: 1f

        framesList.zip(yList).forEach { (frame, yVal) ->
            val px = ((frame - minFrame) / rangeX) * width
            val py = ((yVal - minY) / rangeY) * height

            drawCircle(
                color = zoneColor(yVal),
                radius = 2.dp.toPx(),
                center = Offset(px, py)
            )
        }
    }
}

@Composable
fun PlayerRadarChart(radarData: RadarPerformance, targetPlayer: String) {
    val metrics = radarData.metrics
    val players = radarData.players

    val valuesMatrix = metrics.map { metric ->
        players.map { (_, playerValues) ->
            playerValues[metric] ?: 0f
        }
    }

    val normalizedValues = valuesMatrix.mapIndexed { _, values ->
        val min = values.minOrNull() ?: 0f
        val max = values.maxOrNull() ?: 1f
        val range = (max - min).takeIf { it != 0f } ?: 1f
        val playerIndex = players.keys.indexOf(targetPlayer)
        ((values.getOrNull(playerIndex) ?: 0f) - min) / range
    }

    Canvas(modifier = Modifier.size(230.dp)) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = size.minDimension / 2.5f
        val angleStep = (2 * Math.PI / metrics.size).toFloat()

        val gridLevels = 5
        val step = radius / gridLevels

        for (i in 1..gridLevels) {
            val r = step * i
            val path = Path()
            metrics.forEachIndexed { index, _ ->
                val angle = angleStep * index - Math.PI.toFloat() / 2
                val x = center.x + cos(angle) * r
                val y = center.y + sin(angle) * r
                if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            path.close()
            drawPath(path, color = BlueDark, style = Stroke(width = 3.dp.toPx()))
        }

        metrics.forEachIndexed { i, metric ->
            val angle = angleStep * i - Math.PI.toFloat() / 2
            val end = Offset(
                center.x + cos(angle) * radius,
                center.y + sin(angle) * radius
            )

            drawLine(BlueDark, center, end, strokeWidth = 3.dp.toPx())

            val labelOffset = 24.dp.toPx()
            val labelX = center.x + cos(angle) * (radius + labelOffset)
            val labelY = center.y + sin(angle) * (radius + labelOffset)

            drawContext.canvas.nativeCanvas.drawText(
                metric,
                labelX,
                labelY,
                Paint().apply {
                    color = android.graphics.Color.WHITE
                    textSize = 32f
                    textAlign = Paint.Align.CENTER
                    isFakeBoldText = true
                }
            )
        }

        val path = Path()
        normalizedValues.forEachIndexed { i, value ->
            val angle = angleStep * i - Math.PI.toFloat() / 2
            val x = center.x + cos(angle) * radius * value
            val y = center.y + sin(angle) * radius * value
            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        path.close()

        drawPath(path, color = GreenLight.copy(alpha = 0.7f), style = Fill)

        drawPath(path, color = GreenDark, style = Stroke(width = 2.dp.toPx()))
    }
}

@Composable
fun MatchAnimationWithBall(frames: List<AnimationFrame>, ballTrajectory: BallTrajectory, playerNameMap: Map<String, String?>) {
    if (frames.isEmpty() || ballTrajectory.x.isEmpty() || ballTrajectory.y.isEmpty()) {
        Text("No animation data available", color = White)
        return
    }

    // Animate frame indexes
    var frameIndex by remember { mutableIntStateOf(0) }
    var ballIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(60)
            frameIndex = (frameIndex + 1) % frames.size
            ballIndex = (ballIndex + 1) % ballTrajectory.x.size
        }
    }

    // Fixed coordinate system for players (and ball)
    val fixedXMin = -10f
    val fixedXMax = 10f
    val fixedYMin = -10f
    val fixedYMax = 10f
    val rangeX = fixedXMax - fixedXMin
    val rangeY = fixedYMax - fixedYMin

    // Normalize ball points to fixed coordinate system (-10..10)
    val ballMinX = ballTrajectory.x.minOrNull() ?: fixedXMin
    val ballMaxX = ballTrajectory.x.maxOrNull() ?: fixedXMax
    val ballMinY = ballTrajectory.y.minOrNull() ?: fixedYMin
    val ballMaxY = ballTrajectory.y.maxOrNull() ?: fixedYMax

    fun normalize(value: Float, min: Float, max: Float): Float =
        if (max - min == 0f) 0f else (value - min) / (max - min) * 20f - 10f

    val normalizedBallPoints = ballTrajectory.x.zip(ballTrajectory.y).map { (x, y) ->
        val normX = normalize(x, ballMinX, ballMaxX)
        val normY = normalize(y, ballMinY, ballMaxY)
        normX to normY
    }

    val currentFrame = frames[frameIndex]
    val playerColors = listOf(GreenLight, GreenLight, BlueDark, BlueDark)
    val playerKeys = listOf("player1", "player2", "player3", "player4")

    Canvas(
        modifier = Modifier
            .width(230.dp)
            .height(100.dp)
            .padding(4.dp)
            .background(Transparent)
    ) {
        rotate(degrees = 180f, pivot = Offset(size.width / 2, size.height / 2)) {
            val width = size.width
            val height = size.height

            val positions = listOf(
                currentFrame.player1,
                currentFrame.player2,
                currentFrame.player3,
                currentFrame.player4
            )

            val labelPaint = Paint().apply {
                color = android.graphics.Color.WHITE
                textSize = 20f
                textAlign = Paint.Align.CENTER
                isFakeBoldText = true
            }

            // Draw players
            positions.forEachIndexed { index, pos ->
                val px = ((-pos.y - fixedXMin) / rangeX) * width
                val py = ((-pos.x - fixedYMin) / rangeY) * height
                val color = playerColors.getOrElse(index) { GreenLight }
                val playerName = playerNameMap[playerKeys[index]] ?: playerKeys[index]

                drawCircle(color, radius = 3.dp.toPx(), center = Offset(px, py))

                drawContext.canvas.nativeCanvas.apply {
                    withSave {
                        rotate(-180f, px, py + 18f)
                        drawText(playerName, px, py + 18f, labelPaint)
                    }
                }
            }

            // Draw moving ball
            val (ballX, ballY) = normalizedBallPoints[ballIndex]
            val ballPx = ((-ballY - fixedXMin) / rangeX) * width
            val ballPy = ((-ballX - fixedYMin) / rangeY) * height

            drawCircle(
                color = White,
                radius = 3.dp.toPx(),
                center = Offset(ballPx, ballPy)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalysisAppToolbar(toolbarTitle: String) {
    TopAppBar(
        modifier = Modifier.fillMaxWidth().height(40.dp),
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