package dk.clausr.koncert.ui.onboarding.chatroom

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dk.clausr.koncert.utils.extensions.collectWithLifecycle
import dk.clausr.repo.domain.Group

@Composable
fun JoinOrCreateChatRoomRoute(
    onCreateClicked: (chatRoomId: String) -> Unit,
    onSkipClicked: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: JoinOrCreateChatRoomViewModel = hiltViewModel(),
) {
    viewModel.viewEffects.collectWithLifecycle {
        when (it) {
            is JoinOrCreateChatRoomViewModel.JoinOrCreateChatRoomViewEffects.ChatRoomJoined -> onCreateClicked(
                it.chatRoomId
            )

            JoinOrCreateChatRoomViewModel.JoinOrCreateChatRoomViewEffects.SkipStep -> onSkipClicked()
        }
    }

    val suggestedGroups: List<Group> = emptyList()

    JoinOrCreateChatRoomScreen(
        suggestedGroups = suggestedGroups,
        onJoinOrCreateChatRoom = { chatRoomName ->
            viewModel.setChatRoom(name = chatRoomName)
        },

        onSkip = {
            onSkipClicked()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JoinOrCreateChatRoomScreen(
    modifier: Modifier = Modifier,
    suggestedGroups: List<Group>,
    onJoinOrCreateChatRoom: (chatRoomId: String) -> Unit,
    onSkip: () -> Unit,
) {
    var chatRoomName by remember {
        mutableStateOf("")
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets.statusBars,
                title = {
                    Text(
                        text = "Join chatroom",
                    )
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = chatRoomName,
                onValueChange = {
                    chatRoomName = it
                },
                supportingText = { Text("Create or join group of this name") },
                placeholder = { Text("Groupname") }
            )

            Button(onClick = {
                onJoinOrCreateChatRoom(chatRoomName)
            }) {
                Text("Go to chatroom")
            }

            Button(
                onClick = {

                },
                colors = ButtonDefaults.outlinedButtonColors()
            ) {
                Text("Not now")
            }

            Column {
                suggestedGroups.forEach {
                    Text(
                        text = it.friendlyName,
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                            .clickable {
                                chatRoomName = it.friendlyName
                            },
                    )
                }
            }
        }
    }
}