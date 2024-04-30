package dk.clausr.koncert

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.platform.ComposeView
import dagger.hilt.android.AndroidEntryPoint
import dk.clausr.koncert.ui.KoncertApp
import dk.clausr.koncert.utils.extensions.setKoncertContent


@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(ComposeView(context = this).apply {
            setKoncertContent {
                KoncertApp(calculateWindowSizeClass(activity = this@MainActivity))
            }
        })
    }
}