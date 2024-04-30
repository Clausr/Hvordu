package dk.clausr.koncert.ui.parallax

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import dk.clausr.koncert.utils.sensors.SensorData
import dk.clausr.koncert.utils.sensors.SensorDataManager
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@Composable
fun ParallaxRoute(
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
) {
    ParallaxScreen(
        windowSizeClass = windowSizeClass,
        modifier = modifier
    )
}

@Composable
fun ParallaxScreen(
    windowSizeClass: WindowSizeClass,
    modifier: Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var data by remember { mutableStateOf<SensorData?>(null) }

    DisposableEffect(Unit) {
        val dataManager = SensorDataManager(context)
        dataManager.init()

        val job = scope.launch {
            dataManager.data
                .receiveAsFlow()
                .onEach {
                    data = it
                }
                .collect()
        }

        onDispose {
            dataManager.cancel()
            job.cancel()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        ParallaxComp(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .align(Alignment.Center),
            depthMultiplier = 20,
            data = data
        )
    }
}