package grad.project.padelytics

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import grad.project.padelytics.data.MatchNotificationHelper
import grad.project.padelytics.data.NotificationPreferencesManager
import grad.project.padelytics.navigation.AppNavigation
import grad.project.padelytics.ui.theme.Blue
import grad.project.padelytics.ui.theme.PadelyticsTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
            }
        }

        MatchNotificationHelper.createNotificationChannel(context = this)

        val matchId = intent?.getStringExtra("match_id")
        Log.d("MainActivity", "onCreate: match_id = $matchId")

        if (!matchId.isNullOrEmpty()) {
            getSharedPreferences("match_prefs", MODE_PRIVATE).edit {
                putString("match_id", matchId)
            }
        } else {
            getSharedPreferences("match_prefs", MODE_PRIVATE).edit {
                remove("match_id")
            }
        }

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

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)

        val matchId = intent.getStringExtra("match_id")
        Log.d("MainActivity", "onNewIntent: match_id = $matchId")

        if (!matchId.isNullOrEmpty()) {
            getSharedPreferences("match_prefs", MODE_PRIVATE).edit {
                putString("match_id", matchId)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val allowed = NotificationPreferencesManager.isNotificationAllowed(this)
        val currentPref = NotificationPreferencesManager.notificationState.value

        if (allowed && !currentPref) {
            NotificationPreferencesManager.setNotificationState(this, true)
        }
    }
}