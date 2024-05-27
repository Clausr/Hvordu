package dk.clausr.hvordu.ui.onboarding.notifications

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import timber.log.Timber

@Composable
fun NotificationPermissionRoute(
    modifier: Modifier = Modifier,
    viewModel: NotificationPermissionViewModel = hiltViewModel(),
) {
    NotificationPermissionScreen(
        modifier = modifier,
        onNotificationPermissionAnswered = {
            viewModel.setFirebaseToken()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun NotificationPermissionScreen(
    modifier: Modifier = Modifier,
    onNotificationPermissionAnswered: (Boolean) -> Unit,
) {
    val permissionsLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
            Timber.d("Permission given: $it")
            onNotificationPermissionAnswered(it)
        }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets.statusBars,
                title = {
                    Text(
                        text = "Notifications",
                    )
                },
            )
        },
        contentWindowInsets = WindowInsets.safeDrawing,
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .safeDrawingPadding(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        permissionsLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    },
                ) {
                    Text("Continue")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = "Allow notifications")
            Text(text = "To get updated everytime your friends doesn't want to show you where they are, approve this...")
        }
    }

}