package grad.project.padelytics.features.analysis.components

import android.graphics.Paint
import android.graphics.Typeface
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import grad.project.padelytics.R
import grad.project.padelytics.features.analysis.data.AnimationFrame
import grad.project.padelytics.features.analysis.data.BallSpeedOverTime
import grad.project.padelytics.features.analysis.data.BallTrajectory
import grad.project.padelytics.features.analysis.data.PlayerHeatmap
import grad.project.padelytics.features.analysis.data.PlayerHitLocations
import grad.project.padelytics.features.analysis.data.RadarPerformance
import grad.project.padelytics.features.analysis.data.StrongestHitItem
import grad.project.padelytics.features.analysis.data.TrajectoryData
import grad.project.padelytics.ui.theme.Blue
import grad.project.padelytics.ui.theme.BlueDark
import grad.project.padelytics.ui.theme.GreenDark
import grad.project.padelytics.ui.theme.GreenLight
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
fun PlayersView(){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Blue)
            .padding(16.dp)
            .height(100.dp),
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
                    .size(50.dp)
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

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Player",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = lexendFontFamily,
                    fontWeight = FontWeight.Medium,
                    color = White
                )
            )
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
                    .size(50.dp)
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

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Player",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = lexendFontFamily,
                    fontWeight = FontWeight.Medium,
                    color = White
                )
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Column(modifier = Modifier.fillMaxHeight()
            .wrapContentWidth(),
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

        Column(
            modifier = Modifier.fillMaxHeight()
                .wrapContentWidth(),
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
                Image(
                    painter = painterResource(id = R.drawable.user_selected),
                    contentDescription = "Avatar",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxSize()
                        .border(2.dp, BlueDark, CircleShape)
                        .clip(CircleShape)
                        .background(Transparent)
                )
            }

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Player",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = lexendFontFamily,
                    fontWeight = FontWeight.Medium,
                    color = White
                )
            )
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
                    .size(50.dp)
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
                        .border(2.dp, BlueDark, CircleShape)
                        .clip(CircleShape)
                        .background(Transparent)
                )
            }

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Player",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = lexendFontFamily,
                    fontWeight = FontWeight.Medium,
                    color = White
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
            Image(
                painter = painterResource(R.drawable.left_arrow),
                contentDescription = "Left Arrow",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(20.dp)
                    .clickable {
                        currentIndex = if (currentIndex == 0) graphScreens.lastIndex else currentIndex - 1
                    }
            )

            currentCourtComposable()

            Image(
                painter = painterResource(R.drawable.right_arrow),
                contentDescription = "Right Arrow",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(20.dp)
                    .clickable {
                        currentIndex = (currentIndex + 1) % graphScreens.size
                    }
            )
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
    val points = ballTrajectory.x.zip(ballTrajectory.y)

    Canvas(
        modifier = Modifier
            .width(230.dp)
            .height(100.dp)
            .padding(4.dp)
            .background(Transparent)
    ) {
        val width = size.width
        val height = size.height

        // Determine the min and max values for X and Y axes
        val minX = points.minOf { it.first }
        val maxX = points.maxOf { it.first }
        val minY = points.minOf { it.second }
        val maxY = points.maxOf { it.second }

        // Calculate ranges
        val rangeX = maxX - minX
        val rangeY = maxY - minY

        // Function to map data points to the canvas dimensions
        fun mapX(x: Float) = ((x - minX) / rangeX) * width
        fun mapY(y: Float) = height - ((y - minY) / rangeY) * height

        // Draw the line connecting the points
        points.zipWithNext().forEach { (start, end) ->
            val startX = mapX(start.first)
            val startY = mapY(start.second)
            val endX = mapX(end.first)
            val endY = mapY(end.second)

            drawLine(
                color = GreenLight,
                start = Offset(startX, startY),
                end = Offset(endX, endY),
                strokeWidth = 5f
            )
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
fun TopStrongestHitsBarChart(topHits: List<StrongestHitItem>) {
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
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
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
                "Player ${hit.player}",
                centerX,
                top - 56f,
                labelPaint
            )
        }
    }
}

@Composable
fun HitCountBarChart(hitCount: Map<String, Int>?) {
    val hits = hitCount ?: emptyMap()
    if (hits.isEmpty()) {
        Text("No data available", color = White)
        return
    }

    val playerNames = hits.keys.toList()
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
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        val barCount = hitValues.size
        val spacing = size.width * 0.05f / barCount
        val slotWidth = (size.width - (barCount + 1) * spacing) / barCount
        val barWidth = slotWidth * 0.6f
        val maxHitCount = hitValues.maxOrNull()?.toFloat() ?: 1f

        val topTextHeight = 20.dp.toPx()

        hitValues.forEachIndexed { index, hitCount ->
            val centerX = spacing + index * (slotWidth + spacing) + slotWidth / 2

            // Y calculations
            val barHeight = (hitCount / maxHitCount) * (size.height - topTextHeight - 12.dp.toPx())
            val barTop = size.height - barHeight
            val barLeft = centerX - barWidth / 2

            // 👤 Player label above bar
            drawContext.canvas.nativeCanvas.drawText(
                playerNames[index],
                centerX,
                barTop - 8f,
                labelPaint
            )

            // 🟩 Rounded bar from bottom up
            drawRoundRect(
                color = barColor,
                topLeft = Offset(barLeft, barTop),
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
            )

            // 🔢 Hit count inside bar, vertically centered
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

    Canvas(
        modifier = Modifier
            .width(230.dp)
            .height(100.dp)
            .padding(4.dp)
            .background(Transparent)
    ) {
        val width = size.width
        val height = size.height

        val allPoints = ballHits.flatMap { it.value.x.zip(it.value.y) }

        val minX = allPoints.minOf { it.first }
        val maxX = allPoints.maxOf { it.first }
        val minY = allPoints.minOf { it.second }
        val maxY = allPoints.maxOf { it.second }

        val rangeX = (maxX - minX).takeIf { it != 0f } ?: 1f
        val rangeY = (maxY - minY).takeIf { it != 0f } ?: 1f

        ballHits.entries.forEachIndexed { index, (playerName, playerHits) ->
            val color = playerColors.getOrElse(index) { Color.Gray }

            playerHits.x.zip(playerHits.y).forEach { (x, y) ->
                val px = ((x - minX) / rangeX) * width
                val py = ((-y - (-maxY)) / rangeY) * height

                drawCircle(
                    color = color,
                    radius = 3.dp.toPx(),
                    center = Offset(px, py)
                )
            }
        }
    }
}

@Composable
fun MultiPlayerScatterPlot(playersData: Map<String, TrajectoryData>) {
    val playerColors = listOf(Color.Blue, Color.Red, Color.Magenta, Color.Green)
    val orderedPlayerNames = listOf("player1", "player2", "player4", "player3")

    // Clean and flip X (top-down view)
    val allPoints = playersData.mapValues { (_, playerData) ->
        playerData.x.zip(playerData.y)
            .filter { it.first != null && it.second != null }
            .filter { it.first!!.isFinite() && it.second!!.isFinite() }
            .filterNot { it.first == 0f && it.second == 0f }
            .map { (x, y) -> -x!! to y!! } // Flip X
    }

    if (allPoints.values.all { it.isEmpty() }) {
        Text("No valid points to show", color = MaterialTheme.colorScheme.error)
        return
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Multi-Player Trajectory Plot (Lines, Top-Down View)",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(8.dp)
        )

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .background(White)
        ) {
            // Rotate the canvas 180° around its center
            rotate(degrees = 180f, pivot = Offset(size.width / 2, size.height / 2)) {
                val width = size.width
                val height = size.height

                val fixedYMin = -10f
                val fixedYMax = 10f
                val rangeY = fixedYMax - fixedYMin

                val allX = allPoints.values.flatten().map { it.first }
                val minX = allX.minOrNull() ?: -10f
                val maxX = allX.maxOrNull() ?: 10f
                val rangeX = (maxX - minX).takeIf { it != 0f } ?: 1f

                // Draw court and net
                drawRect(color = Color(0xFFCCE5FF), size = size)
                val netX = ((0f - fixedYMin) / rangeY) * width
                drawLine(Color.Black, Offset(netX, 0f), Offset(netX, height), 2.dp.toPx())

                // Grid (optional)
                val gridCount = 10
                val stepX = width / gridCount
                val stepY = height / gridCount
                for (i in 0..gridCount) {
                    drawLine(Color.LightGray, Offset(i * stepX, 0f), Offset(i * stepX, height), 1.dp.toPx())
                    drawLine(Color.LightGray, Offset(0f, i * stepY), Offset(width, i * stepY), 1.dp.toPx())
                }

                // Draw lines for each player
                orderedPlayerNames.forEachIndexed { index, playerName ->
                    val points = allPoints[playerName] ?: return@forEachIndexed
                    if (points.size < 2) return@forEachIndexed

                    val color = playerColors.getOrElse(index) { Color.Black }

                    val path = Path().apply {
                        val first = points.first()
                        moveTo(
                            x = ((first.second - fixedYMin) / rangeY) * width,
                            y = ((first.first - minX) / rangeX) * height
                        )
                        points.drop(1).forEach { (x, y) ->
                            val px = ((y - fixedYMin) / rangeY) * width
                            val py = ((x - minX) / rangeX) * height
                            lineTo(px, py)
                        }
                    }

                    drawPath(path, color = color, style = Stroke(width = 2.dp.toPx()))
                }
            }
        }
    }
}

@Composable
fun AnimatedPlayerScatterPlot(frames: List<AnimationFrame>) {
    if (frames.isEmpty()) {
        Text("No animation data available")
        return
    }

    val playerColors = listOf(Color.Blue, Color.Red, Color.Green, Color.Magenta)

    var frameIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(60) // ~16 FPS
            frameIndex = (frameIndex + 1) % frames.size
        }
    }

    val currentFrame = frames[frameIndex]

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .background(Color(0xFFCCE5FF))
    ) {
        rotate(degrees = 180f, pivot = Offset(size.width / 2, size.height / 2)) {
            val width = size.width
            val height = size.height

            // Axis range (same for all players)
            val fixedXMin = -10f
            val fixedXMax = 10f
            val rangeX = fixedXMax - fixedXMin

            val yRangeMin = -10f
            val yRangeMax = 10f
            val rangeY = yRangeMax - yRangeMin

            // Draw net
            val netX = ((0f - fixedXMin) / rangeX) * width
            drawLine(Color.Black, Offset(netX, 0f), Offset(netX, height), 3.dp.toPx())

            val playerPositions = listOf(
                currentFrame.player1 to playerColors[0],
                currentFrame.player2 to playerColors[1],
                currentFrame.player3 to playerColors[2],
                currentFrame.player4 to playerColors[3]
            )

            playerPositions.forEach { (pos, color) ->
                val px = ((pos.y - fixedXMin) / rangeX) * width
                val py = ((-pos.x - yRangeMin) / rangeY) * height
                drawCircle(color, radius = 10.dp.toPx(), center = Offset(px, py))
            }
        }
    }
}

@Composable
fun MultiPlayerHeatmap(heatmaps: Map<String, PlayerHeatmap>) {
    val playerColors = listOf(Color.Blue, Color.Red, Color.Green, Color.Magenta)

    val allPoints = heatmaps.mapValues { (_, playerData) ->
        playerData.x.zip(playerData.y)
            .filter { it.first.isFinite() && it.second.isFinite() }
            .filterNot { it.first == 0f && it.second == 0f }
    }

    if (allPoints.values.all { it.isEmpty() }) {
        Text("No valid heatmap data available", color = MaterialTheme.colorScheme.error)
        return
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Multi-Player Heatmap (From Heatmap Data)",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(8.dp)
        )

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {
            rotate(degrees = 180f, pivot = Offset(size.width / 2, size.height / 2)) {
                val width = size.width
                val height = size.height
                val gridSize = 20

                val minX = allPoints.values.flatten().minOf { it.first }
                val maxX = allPoints.values.flatten().maxOf { it.first }
                val minY = allPoints.values.flatten().minOf { it.second }
                val maxY = allPoints.values.flatten().maxOf { it.second }

                val rangeX = (maxX - minX).takeIf { it != 0f } ?: 1f
                val rangeY = (maxY - minY).takeIf { it != 0f } ?: 1f

                val cellWidth = width / gridSize
                val cellHeight = height / gridSize

                // Draw court background
                drawRect(color = Color(0xFFCCE5FF), size = size)

                // Center lines
                val midX = width / 2
                val midY = height / 2
                drawLine(White, Offset(midX, 0f), Offset(midX, height), 4.dp.toPx())
                drawLine(White, Offset(0f, midY), Offset(width, midY), 4.dp.toPx())

                // Draw data points
                heatmaps.entries.forEachIndexed { index, (player, heatmap) ->
                    val color = playerColors.getOrNull(index) ?: Color.Black
                    heatmap.x.zip(heatmap.y).forEach { (x, y) ->
                        val px = ((y - minY) / rangeY) * width
                        val py = height - ((x - minX) / rangeX) * height

                        drawCircle(
                            color = color.copy(alpha = 0.2f),
                            radius = 10.dp.toPx(),
                            center = Offset(px, py)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PlayerBarChart(
    title: String,
    yAxisTitle: String = "",
    playerValues: Map<String, Float>
) {
    val playerColors = listOf(Color.Blue, Color.Red, Color.Green, Color.Magenta)
    val barLabelStyle = MaterialTheme.typography.labelSmall

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            val width = size.width
            val height = size.height
            val barCount = playerValues.size
            val barWidth = width / (barCount * 2f)

            val maxVal = playerValues.values.maxOrNull() ?: 1f

            // Y-axis + grid
            val step = maxVal / 5f
            for (i in 0..5) {
                val yVal = step * i
                val yPx = height - (yVal / maxVal) * height
                drawLine(
                    color = Color.LightGray,
                    start = Offset(0f, yPx),
                    end = Offset(width, yPx),
                    strokeWidth = 1.dp.toPx()
                )
                drawContext.canvas.nativeCanvas.drawText(
                    "%.1f".format(yVal),
                    4.dp.toPx(),
                    yPx - 4.dp.toPx(),
                    Paint().apply {
                        color = android.graphics.Color.DKGRAY
                        textSize = 24f
                    }
                )
            }

            // Draw bars
            playerValues.entries.forEachIndexed { index, entry ->
                val value = entry.value
                var color = playerColors.getOrNull(index) ?: Color.Black

                val left = (index * 2 + 1) * barWidth
                val top = height - (value / maxVal) * height
                val right = left + barWidth
                val bottom = height

                drawRect(
                    color = color,
                    topLeft = Offset(left, top),
                    size = Size(right - left, bottom - top)
                )

                // Player name under bar
                drawContext.canvas.nativeCanvas.drawText(
                    entry.key,
                    left + barWidth / 4,
                    height + 24.dp.toPx(),
                    Paint().apply {
                        color = Color.Black
                        textSize = 28f
                    }
                )
            }

            // Y-Axis label
            drawContext.canvas.nativeCanvas.drawText(
                yAxisTitle,
                0f,
                16.dp.toPx(),
                Paint().apply {
                    color = android.graphics.Color.BLACK
                    textSize = 32f
                    isFakeBoldText = true
                }
            )
        }
    }
}

@Composable
fun PlayerRadarChart(radarData: RadarPerformance) {
    val colors = listOf(Color.Blue.copy(alpha = 0.5f), Color.Red.copy(alpha = 0.5f), Color.Green.copy(alpha = 0.5f), Color.Magenta.copy(alpha = 0.5f))
    val players = radarData.players
    val metrics = radarData.metrics

    // 1. Normalize all player values to [0, 1] per metric
    val maxPerMetric = mutableMapOf<String, Float>()
    metrics.forEach { metric ->
        val maxVal = players.values.maxOfOrNull { it[metric] ?: 0f } ?: 1f
        maxPerMetric[metric] = maxVal
    }

    val normalizedPlayers = players.mapValues { (_, playerMetrics) ->
        playerMetrics.mapValues { (metric, value) ->
            val maxVal = maxPerMetric[metric] ?: 1f
            (value / maxVal).coerceIn(0f, 1f)
        }
    }

    // 2. Now draw
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .padding(16.dp)
    ) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = size.minDimension / 2.5f
        val angleStep = (2 * Math.PI / metrics.size).toFloat()

        val gridLevels = 5
        val step = radius / gridLevels

        // Draw radial grid circles (or polygons)
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
            drawPath(
                path = path,
                color = Color.LightGray,
                style = Stroke(width = 1.dp.toPx())
            )
        }

        // Draw axes
        metrics.forEachIndexed { i, metric ->
            val angle = angleStep * i - Math.PI.toFloat() / 2
            val end = Offset(
                center.x + cos(angle) * radius,
                center.y + sin(angle) * radius
            )
            drawLine(Color.LightGray, center, end, strokeWidth = 2.dp.toPx())

            // Metric labels
            val labelOffset = 24.dp.toPx()
            val labelX = center.x + cos(angle) * (radius + labelOffset)
            val labelY = center.y + sin(angle) * (radius + labelOffset)
            drawContext.canvas.nativeCanvas.drawText(
                metric,
                labelX,
                labelY,
                Paint().apply {
                    color = android.graphics.Color.BLACK
                    textSize = 28f
                    textAlign = Paint.Align.CENTER
                }
            )
        }

        // Draw players
        normalizedPlayers.entries.forEachIndexed { index, (playerName, normalizedValuesMap) ->
            val path = Path()
            metrics.forEachIndexed { i, metric ->
                val normalized = normalizedValuesMap[metric] ?: 0f
                val angle = angleStep * i - Math.PI.toFloat() / 2
                val x = center.x + cos(angle) * radius * normalized
                val y = center.y + sin(angle) * radius * normalized
                if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            path.close()

            drawPath(
                path,
                color = colors.getOrElse(index) { Color.Black },
                style = Fill
            )

            drawPath(
                path,
                color = colors.getOrElse(index) { Color.Black }.copy(alpha = 0.8f),
                style = Stroke(width = 2.dp.toPx())
            )
        }
    }
}