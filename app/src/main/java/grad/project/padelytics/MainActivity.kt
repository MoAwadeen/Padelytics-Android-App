package grad.project.padelytics

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.graphics.toArgb
import grad.project.padelytics.navigation.AppNavigation
import grad.project.padelytics.ui.theme.Blue
import grad.project.padelytics.ui.theme.PadelyticsTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Blue.toArgb(),Blue.toArgb()),
            navigationBarStyle = SystemBarStyle.light(Blue.toArgb(),Blue.toArgb())
        )

        setContent {
            PadelyticsTheme {
                  AppNavigation()
                }
            }
        }
    }


