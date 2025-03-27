package grad.project.padelytics.features.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import grad.project.padelytics.R
import grad.project.padelytics.ui.theme.Blue
import grad.project.padelytics.ui.theme.GreenLight
import grad.project.padelytics.ui.theme.lexendFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeAppToolbar(userName: String) {
    TopAppBar(
        modifier = Modifier.fillMaxWidth().height(80.dp),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Blue,
            titleContentColor = White,
        ),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.padelytics_2),
                    contentDescription = "Logo",
                    modifier = Modifier.width(160.dp).height(36.dp).padding(start = 3.dp)
                )
                Text(
                    text = "Hello, $userName !",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontFamily = lexendFontFamily,
                        fontWeight = FontWeight.Normal,
                        color = White
                    ),
                    modifier = Modifier.padding(end = 20.dp)
                )
            }
        }
    )
}

@Preview
@Composable
fun HomeAppToolbarPreview(){
    HomeAppToolbar(userName = "Merna")
}
