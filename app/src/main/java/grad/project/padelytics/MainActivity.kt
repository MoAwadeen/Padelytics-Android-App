package grad.project.padelytics

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import grad.project.padelytics.navigation.AppNavigation
import grad.project.padelytics.ui.theme.PadelyticsTheme
import grad.project.padelytics.ui.theme.*
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Blue.toArgb(),Blue.toArgb()),
            navigationBarStyle = SystemBarStyle.light(Blue.toArgb(),Blue.toArgb())
        )

        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition {true}

        CoroutineScope(Dispatchers.Main).launch {
            delay(2000)
            splashScreen.setKeepOnScreenCondition {false}
        }

        setContent {
            PadelyticsTheme {
                  AppNavigation(modifier = Modifier)
            }
        }
    }
}


