package grad.project.padelytics.data

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object NotificationPreferencesManager {
    private const val PREFS_NAME = "notification_prefs"
    private const val KEY_ENABLED = "notifications_enabled"
    private const val KEY_INITIALIZED = "notification_initialized"

    private val _notificationState = MutableStateFlow(true)
    val notificationState: StateFlow<Boolean> = _notificationState

    fun loadState(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val isInitialized = prefs.getBoolean(KEY_INITIALIZED, false)

        if (!isInitialized) {
            // First launch! Set based on permission.
            val enabled = isNotificationAllowed(context)
            prefs.edit {
                putBoolean(KEY_ENABLED, enabled)
                    .putBoolean(KEY_INITIALIZED, true)
            }
            _notificationState.value = enabled
        } else {
            _notificationState.value = prefs.getBoolean(KEY_ENABLED, true)
        }
    }

    fun setNotificationState(context: Context, enabled: Boolean) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit {
                putBoolean(KEY_ENABLED, enabled)
            }
        _notificationState.value = enabled
    }

    fun isNotificationAllowed(context: Context): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
    }
}