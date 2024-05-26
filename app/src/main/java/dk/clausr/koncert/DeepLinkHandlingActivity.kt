package dk.clausr.koncert

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import dk.clausr.koncert.ui.compose.theme.KoncertTheme
import dk.clausr.koncert.ui.onboarding.username.CreateUserScreen
import dk.clausr.koncert.utils.extensions.collectWithLifecycle
import io.github.jan.supabase.SupabaseClient
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class DeepLinkHandlingActivity : ComponentActivity() {
    @Inject
    lateinit var supabaseClient: SupabaseClient

    private val viewModel: DeepLinkHandlingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.handleLoginIntent(intent)

        enableEdgeToEdge()

        Timber.d("OnCreate deeplink handler.. $intent")

        setContent {
            val existingUsername by viewModel.username.collectAsState()
            viewModel.viewEffects.collectWithLifecycle {
                when (it) {
                    DeepLinkHandlingViewEffect.NavigateToHome -> navigateToMainApp()
                }
            }

            val userSession by viewModel.userSession.collectAsState(initial = null)

            val userInfo by remember(userSession) { mutableStateOf(userSession?.user) }

            Timber.d("Session user id ${userSession?.user?.id}")

            KoncertTheme {
                CreateUserScreen(
                    modifier = Modifier.fillMaxSize(),
                    existingUsername = existingUsername,
                    onCreateClicked = {
                        viewModel.setUsername(it)
                    }
                )
            }
        }
    }

    private fun navigateToMainApp() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("Test", "Authenticated all good")
        }
        startActivity(intent)
    }
}