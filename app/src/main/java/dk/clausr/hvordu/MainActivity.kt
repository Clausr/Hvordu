package dk.clausr.hvordu

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dk.clausr.hvordu.ui.HvorduApp
import dk.clausr.hvordu.ui.compose.theme.HvorduTheme
import io.github.jan.supabase.SupabaseClient
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var supabase: SupabaseClient

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        Timber.d("On new intent ${intent.toUri(0)}")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        enableEdgeToEdge()

        super.onCreate(savedInstanceState)

        val newSignIn = intent.getBooleanExtra(/* name = */ NEW_SIGN_IN, /* defaultValue = */ false)

        Timber.d("New Sign in? -> ${intent.toUri(0)}")
//        Timber.d("New Sign in? -> $newSignIn")

        var uiState: MainActivityUiState by mutableStateOf(MainActivityUiState.Loading)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState
                    .onEach { uiState = it }
                    .collect()
            }
        }

        splashScreen.setKeepOnScreenCondition {
            when (uiState) {
                MainActivityUiState.Loading -> true
                else -> false
            }
        }

        setContent {
            val navHostController = rememberNavController()
            val appState = rememberHvorduAppState(
                windowSizeClass = calculateWindowSizeClass(
                    activity = this@MainActivity
                ),
                navController = navHostController,
            )

            HvorduTheme {
                HvorduApp(
                    windowSizeClass = calculateWindowSizeClass(activity = this@MainActivity),
                    appState = appState,
                )
            }
        }
    }

    companion object {
        const val NEW_SIGN_IN = "NewSignIn"
    }
}
