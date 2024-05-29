package dk.clausr.hvordu.notifications

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import dk.clausr.hvordu.repo.userdata.UserRepository
import io.github.jan.supabase.gotrue.Auth
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class HvorduFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var auth: Auth

    @Inject
    lateinit var notificationsPresenter: NotificationsPresenter

    override fun onCreate() {
        super.onCreate()
        notificationsPresenter.registerChannels()
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Timber.d("New firebase token: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        Timber.d("On message receieved: ${message.data["profile_id"]} -- ${auth.currentUserOrNull()?.id}")
        // Filter away notifications that were sent because of current user
        if (message.data["profile_id"] == auth.currentUserOrNull()?.id) return


        notificationsPresenter.showNotification(
            title = message.notification?.title ?: "No title",
            contentText = message.notification?.body ?: "No body",
            tag = message.notification?.tag,
            id = 0,
            notificationChannel = HvorduNotificationChannel.ChatNotifications,
        )
        Timber.d("On message received! $message -- current auth id = ${auth.currentUserOrNull()?.id}")
    }
}