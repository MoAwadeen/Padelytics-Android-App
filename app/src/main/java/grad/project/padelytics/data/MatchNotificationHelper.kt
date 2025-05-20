package grad.project.padelytics.data

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import grad.project.padelytics.MainActivity
import grad.project.padelytics.R

object MatchNotificationHelper {
    private const val CHANNEL_ID = "match_channel"
    private const val CHANNEL_NAME = "Match Notifications"
    private const val CHANNEL_DESC = "Notifies when a match is saved"

    fun createNotificationChannel(context: Context) {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
            description = CHANNEL_DESC
        }
        val manager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun sendMatchSavedNotification(context: Context, matchId: String) {
        // 🔍 1. Check if notifications are allowed (API 33+) and user preference is enabled
        val isPrefEnabled = NotificationPreferencesManager.notificationState.value
        val isPermissionGranted = NotificationPreferencesManager.isNotificationAllowed(context)

        if (!isPrefEnabled || !isPermissionGranted) {
            Log.d("NotificationDebug", "Notification blocked: enabled=$isPrefEnabled, permission=$isPermissionGranted")
            return
        }

        // 🔗 2. Build Intent to launch AnalysisScreen via matchId
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("match_id", matchId)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // 🔔 3. Create and send the notification
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.racket)
            .setContentTitle("Match Analysis")
            .setContentText("Your match analysis is ready!🎉")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        Log.d("NotificationDebug", "✅ Notification sent with matchId = $matchId")
        NotificationManagerCompat.from(context).notify(1001, notification)
    }
}