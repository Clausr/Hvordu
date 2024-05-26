package dk.clausr.koncert

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import dk.clausr.koncert.ui.compose.theme.KoncertTheme
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

        Timber.d("OnCreate deeplink handler.. $intent")
        viewModel.handleLoginIntent(intent)

        setContent {
            val userSession by viewModel.userSession.collectAsState(initial = null)

            val emailState = remember(userSession) { mutableStateOf(userSession?.user) }
            val createdAtState = remember { mutableStateOf("") }

            KoncertTheme {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(text = "Sign in successfully!")
                    Text(text = "Email ${emailState.value}")
                    Text(text = "Created at ${createdAtState.value}")
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        onClick = { navigateToMainApp() }
                    ) {
                        Text("Continue")
                    }
                }
            }
        }
    }

    private fun navigateToMainApp() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        startActivity(intent)
    }
}