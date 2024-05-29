package dk.clausr.hvordu.notifications

import dk.clausr.hvordu.R

enum class HvorduNotificationChannel {
    ChatNotifications {
        override val channelId = "dk.clausr.hvordu.push.chat_notifications"
        override val channelName = R.string.chat_notification_name
        override val channelDescription = R.string.chat_notification_description
        override val requestCode = 1337
    };

    abstract val channelId: String
    abstract val channelName: Int
    abstract val channelDescription: Int
    abstract val requestCode: Int
}