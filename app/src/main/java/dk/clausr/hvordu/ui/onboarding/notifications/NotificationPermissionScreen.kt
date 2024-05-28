package dk.clausr.hvordu.ui.onboarding.notifications

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dk.clausr.hvordu.ui.compose.theme.HvorduTheme
import dk.clausr.hvordu.utils.extensions.collectWithLifecycle

@Composable
fun NotificationPermissionRoute(
    modifier: Modifier = Modifier,
    viewModel: NotificationPermissionViewModel = hiltViewModel(),
    onNavigateToChatRoomOverview: () -> Unit,
) {

    viewModel.viewEffects.collectWithLifecycle {
        when (it) {
            NotificationPermissionViewEffect.NavigateToChatRoomOverview -> {
                onNavigateToChatRoomOverview()
            }
        }
    }

    NotificationPermissionScreen(
        modifier = modifier,
        onNotificationPermissionAnswered = {
            viewModel.setFirebaseToken()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationPermissionScreen(
    modifier: Modifier = Modifier,
    onNotificationPermissionAnswered: (Boolean) -> Unit,
) {
    val permissionsLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
            onNotificationPermissionAnswered(it)
        }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets.statusBars,
                title = {
                    Text(
                        text = "Allow notifications",
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
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(
                8.dp,
                alignment = Alignment.CenterVertically
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = "To get updated everytime your friends doesn't want to show you where they are, approve this...")
        }
    }
}

@Preview
@Composable
fun NotificationScreenPreview(

) {
    HvorduTheme {
        NotificationPermissionScreen {

        }
    }
}