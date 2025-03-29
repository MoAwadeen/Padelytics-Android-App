package grad.project.padelytics

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import grad.project.padelytics.navigation.AppNavigation
import grad.project.padelytics.ui.theme.PadelyticsTheme
import grad.project.padelytics.ui.theme.*
import grad.project.padelytics.ui.theme.lexendFontFamily
import android.graphics.Color.*
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.colorResource


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Blue.toArgb(),Blue.toArgb()),
            navigationBarStyle = SystemBarStyle.light(Blue.toArgb(),Blue.toArgb())
        )

        setContent {
            PadelyticsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                  AppNavigation(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}


