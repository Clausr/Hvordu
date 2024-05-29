package dk.clausr.hvordu.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.app.NotificationCompat
import dk.clausr.hvordu.R
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationsPresenter @Inject constructor(
    private val applicationContext: Context
) {
    private val context: Context
        get() = ContextThemeWrapper(applicationContext, R.style.Theme_Hvordu)

    private val notificationManager by lazy {
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    fun showNotification(
        title: String,
        contentText: String,
        tag: String?,
        id: Int,
        notificationChannel: HvorduNotificationChannel,
    ) {
        Timber.d("Show notification: $title - $contentText")

        val notification = NotificationCompat.Builder(context, notificationChannel.channelId)
            .setContentTitle(title)
            .setContentText(contentText)
            .setSmallIcon(R.drawable.ic_notifications_black_24dp)
            .setStyle(NotificationCompat.BigTextStyle().bigText(contentText))
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setChannelId(notificationChannel.channelId)
            .build()

        notificationManager.notify(tag, id, notification)
    }

    fun registerChannels() {
        HvorduNotificationChannel
            .entries
            .forEach { hvorduChannel ->
                notificationManager.createNotificationChannel(getNotificationChannel(hvorduChannel))
            }
    }


    private fun getNotificationChannel(hvorduNotificationChannel: HvorduNotificationChannel): NotificationChannel {
        return NotificationChannel(
            hvorduNotificationChannel.channelId,
            context.getString(hvorduNotificationChannel.channelName),
            NotificationManager.IMPORTANCE_HIGH,
        ).apply {
            description = context.getString(hvorduNotificationChannel.channelDescription)
            enableLights(true)
            enableVibration(true)
        }
    }
}