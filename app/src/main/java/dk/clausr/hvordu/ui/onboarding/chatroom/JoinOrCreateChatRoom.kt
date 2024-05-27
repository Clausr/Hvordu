package dk.clausr.hvordu.ui.onboarding.chatroom

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dk.clausr.hvordu.repo.domain.Group
import dk.clausr.hvordu.ui.compose.theme.HvorduTheme
import dk.clausr.hvordu.utils.extensions.collectWithLifecycle

@Composable
fun JoinOrCreateChatRoomRoute(
    onCreateClicked: (chatRoomId: String) -> Unit,
    onSkipClicked: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: JoinOrCreateChatRoomViewModel = hiltViewModel(),
) {
    viewModel.viewEffects.collectWithLifecycle {
        when (it) {
            is JoinOrCreateChatRoomViewModel.JoinOrCreateChatRoomViewEffects.ChatRoomJoined -> {
//                onCreateClicked(
//                    it.chatRoomId
//                )
                onSkipClicked()
            }

            JoinOrCreateChatRoomViewModel.JoinOrCreateChatRoomViewEffects.SkipStep -> {
                onSkipClicked()
            }
        }
    }

    val suggestedGroups: List<Group> = emptyList()

    JoinOrCreateChatRoomScreen(
        suggestedGroups = suggestedGroups,
        onJoinOrCreateChatRoom = { chatRoomName ->
            viewModel.setChatRoom(name = chatRoomName)
        },
        onSkip = {
            viewModel.skip()
//            onSkipClicked()
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

    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
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
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .safeContentPadding(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    enabled = chatRoomName.isNotBlank(),
                    onClick = {
                        onJoinOrCreateChatRoom(chatRoomName)
                    },
                ) {
                    Text("Add chat room")
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
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                value = chatRoomName,
                onValueChange = {
                    chatRoomName = it
                },
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { onJoinOrCreateChatRoom(chatRoomName) }),
                supportingText = { Text("Create or join group of this name") },
                placeholder = { Text("Groupname") }
            )

            Column(
                modifier = Modifier.padding(top = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Suggested groups",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.align(Alignment.Start)
                )
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                    Column {
                        suggestedGroups.take(5).forEachIndexed { index, group ->
                            Text(
                                text = group.friendlyName,
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth()
                                    .clickable {
                                        chatRoomName = group.friendlyName
                                    },
                            )
                            if (index < suggestedGroups.size - 1) HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun JoinOrCreateChatRoomPreview() {
    HvorduTheme {
        JoinOrCreateChatRoomScreen(
            modifier = Modifier.fillMaxSize(),
            suggestedGroups = listOf(
                Group("0", "Rock Am Ring 2024"),
                Group("1", "Roskilde 2024"),
            ),
            onJoinOrCreateChatRoom = {},
            onSkip = {},
        )
    }
}