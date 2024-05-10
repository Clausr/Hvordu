package dk.clausr.koncert.ui.camera

import android.Manifest
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import dk.clausr.koncert.ui.compose.theme.KoncertTheme
import dk.clausr.koncert.utils.sensors.SensorData
import dk.clausr.koncert.utils.sensors.SensorDataManager
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlin.math.abs

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AnnoyingCameraRoute(
    modifier: Modifier = Modifier,
    onCloseClicked: () -> Unit,
) {
    val cameraPermissionState = rememberPermissionState(
        Manifest.permission.CAMERA
    )

    Box(modifier) {
        if (cameraPermissionState.status.isGranted) {
            AnnoyingCameraScreen()
        } else {
            CameraPermissionScreen(
                cameraPermissionState = cameraPermissionState
            )
        }

        IconButton(onClick = onCloseClicked) {
            Icon(Icons.Default.Close, contentDescription = null)
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraPermissionScreen(
    modifier: Modifier = Modifier,
    cameraPermissionState: PermissionState,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val textToShow = if (cameraPermissionState.status.shouldShowRationale) {
            // If the user has denied the permission but the rationale can be shown,
            // then gently explain why the app requires this permission
            "The camera is important for this app. Please grant the permission."
        } else {
            // If it's the first time the user lands on this feature, or the user
            // doesn't want to be asked again for this permission, explain that the
            // permission is required
            "Camera permission required for this feature to be available. " +
                    "Please grant the permission"
        }
        Text(
            text = textToShow,
            modifier = Modifier
                .fillMaxWidth()
                .padding(KoncertTheme.dimensions.padding16),
            textAlign = TextAlign.Center
        )
        Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
            Text("Request permission")
        }
    }
}

@Composable
fun AnnoyingCameraScreen(
    modifier: Modifier = Modifier,
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

    val roll by remember(data?.roll) { derivedStateOf { (data?.roll ?: 0f) * 10f } }
    val pitch by remember(data?.pitch) { derivedStateOf { (data?.pitch ?: 0f) * 10f } }

    val fuzzyRange = 5f

    val isLookingDown by remember(roll, pitch) {
        derivedStateOf { (roll != 0f && pitch != 0f) && abs(roll) < fuzzyRange && abs(pitch) < fuzzyRange }
    }

    val overlayColor by animateColorAsState(
        targetValue = if (isLookingDown) Color.Transparent else MaterialTheme.colorScheme.onBackground,
    )

    Box(modifier) {
        CameraPreviewScreen(
            modifier = Modifier,
            enableTakeImageButton = isLookingDown
        )

        if (!isLookingDown) {
            // Either make the color depend on how close to the wanted value we have or create a graphic element
            Box(
                Modifier
                    .fillMaxSize()
                    .background(overlayColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Roll: $roll - Pitch: $pitch",
                    color = MaterialTheme.colorScheme.background
                )
            }
        }
    }
}

