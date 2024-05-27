package dk.clausr.hvordu.notifications

import com.google.firebase.messaging.FirebaseMessagingService
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class HvorduFirebaseMessagingService : FirebaseMessagingService() {

    override fun onCreate() {
        super.onCreate()

    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Timber.d("New firebase token: $token")

    }
}