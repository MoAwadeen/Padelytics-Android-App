package grad.project.padelytics.features.videoUpload.components

import android.graphics.Color.TRANSPARENT
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import grad.project.padelytics.R
import grad.project.padelytics.ui.theme.Blue
import grad.project.padelytics.ui.theme.BlueDark
import grad.project.padelytics.ui.theme.GreenLight
import grad.project.padelytics.ui.theme.WhiteGray
import grad.project.padelytics.ui.theme.lexendFontFamily

@Composable
fun VideoUploadCard(uploadOnClick: () -> Unit) {
    Image(
        painter = painterResource(id = R.drawable.upload_video),
        contentDescription = "upload icon",
        modifier = Modifier
            .height(180.dp)
            .fillMaxWidth()
            .clickable { uploadOnClick() }
            .padding(horizontal = 18.dp, vertical = 0.dp),
        contentScale = ContentScale.Fit,
        alignment = Alignment.Center
    )
}

@Preview
@Composable
fun VideoUploadPreview() {
    VideoUploadCard(){}
}




@Composable
fun PlayersGrid(options: List<String>, onSelectionChange: (String) -> Unit) {
    var selectedButton by remember { mutableStateOf<Int?>(null) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        options.chunked(2).forEach { rowButtons ->
            Row {
                rowButtons.forEach { text ->
                    val buttonIndex = options.indexOf(text)
                    Button(
                        onClick = {
                            selectedButton = buttonIndex
                            onSelectionChange(text) // Notify parent about the selected option
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedButton == buttonIndex) GreenLight else WhiteGray
                        ),
                        border = BorderStroke(5.dp, if (selectedButton == buttonIndex) GreenLight else GreenLight),
                        shape = RoundedCornerShape(100.dp),
                        elevation = ButtonDefaults.buttonElevation(0.dp),
                        modifier = Modifier
                            .padding(8.dp)
                            .weight(1f)
                            .width(140.dp)
                            .height(74.dp)
                    ) {

                       Row(
                           verticalAlignment = Alignment.CenterVertically,
                           horizontalArrangement = Arrangement.Center,
                           modifier = Modifier.fillMaxWidth(),
                       ) {
                           Image(
                           painter = painterResource(id = R.drawable.user),
                           contentDescription = "Avatar",
                           contentScale = ContentScale.Fit,
                           modifier = Modifier
                               .size(50.dp)
                               .border(2.dp, BlueDark, CircleShape)
                               .clip(CircleShape)
                               .background(Transparent)
                       )
                           Spacer(modifier = Modifier.width(8.dp))
                           Text(
                               text = text,
                               fontSize = 22.sp,
                               color = if (selectedButton == buttonIndex) BlueDark else BlueDark,
                               fontFamily = lexendFontFamily,
                               fontWeight = if (selectedButton == buttonIndex) FontWeight.Bold else FontWeight.SemiBold,
                               maxLines = 1,
                           ) }

                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PlayersGridPreview() {
    val options = listOf("Player 1", "Player 2", "Player 3", "Player 4")
    PlayersGrid(options = options, onSelectionChange = {})
}
