package dk.clausr.koncert

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dk.clausr.koncert.ui.compose.theme.HvorduTheme
import dk.clausr.koncert.ui.onboarding.navigation.CREATE_USER_ROUTE
import dk.clausr.koncert.ui.onboarding.navigation.JOIN_CHAT_ROOM_ROUTE
import dk.clausr.koncert.ui.onboarding.navigation.onboardingGraph
import dk.clausr.koncert.ui.widgets.LoadingScreen
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
            val uiState by viewModel.uiState.collectAsState(DeepLinkUiState.Loading)

            viewModel.viewEffects.collectWithLifecycle {
                when (it) {
                    DeepLinkHandlingViewEffect.NavigateToHome -> navigateToMainApp()
                }
            }

            val navController = rememberNavController()
            val userSession by viewModel.userSession.collectAsState(initial = null)

            Timber.d("Session user id ${userSession?.user?.id}")

            HvorduTheme {
                Scaffold(
                    contentWindowInsets = WindowInsets(0, 0, 0, 0),
                ) { padding ->
                    when (val state = uiState) {
                        DeepLinkUiState.Loading -> {
                            LoadingScreen(Modifier.fillMaxSize())
                        }

                        is DeepLinkUiState.Success -> {
                            NavHost(
                                navController = navController,
                                startDestination = if (state.username == null) CREATE_USER_ROUTE else JOIN_CHAT_ROOM_ROUTE,
                                modifier = Modifier
                                    .padding(padding)
                                    .consumeWindowInsets(padding),
                            ) {
                                onboardingGraph(navController)
                            }

                        }
                    }
                }
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