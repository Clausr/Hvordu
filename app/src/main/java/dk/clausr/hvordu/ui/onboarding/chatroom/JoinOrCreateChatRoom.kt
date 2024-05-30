package dk.clausr.hvordu.ui.onboarding.chatroom

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dk.clausr.hvordu.repo.domain.Group
import dk.clausr.hvordu.ui.compose.theme.HvorduTheme
import dk.clausr.hvordu.ui.widgets.HvorduLazyColumnScaffold
import dk.clausr.hvordu.utils.extensions.collectWithLifecycle

@Composable
fun JoinOrCreateChatRoomRoute(
    onNextClicked: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: JoinOrCreateChatRoomViewModel = hiltViewModel(),
) {
    viewModel.viewEffects.collectWithLifecycle {
        when (it) {
            is JoinOrCreateChatRoomViewModel.JoinOrCreateChatRoomViewEffects.ChatRoomJoined -> {
                onNextClicked()
            }
        }
    }

    val suggestedGroups: List<Group> by viewModel.groups.collectAsStateWithLifecycle(initialValue = emptyList())

    JoinOrCreateChatRoomScreen(
        modifier = modifier,
        suggestedGroups = suggestedGroups,
        onJoinOrCreateChatRoom = { chatRoomName ->
            viewModel.setChatRoom(name = chatRoomName)
        },
    )
}

@Composable
fun JoinOrCreateChatRoomScreen(
    modifier: Modifier = Modifier,
    suggestedGroups: List<Group>,
    onJoinOrCreateChatRoom: (chatRoomId: String) -> Unit,
) {
    var chatRoomName by remember {
        mutableStateOf("")
    }

    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    HvorduLazyColumnScaffold(
        modifier = modifier,
        title = "Join chatroom",
        lazyColumnContentPadding = PaddingValues(16.dp),
        bottomBarContent = {
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
    ) {
        item {
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
                supportingText = { Text("Create or join a chat room with this name") },
                placeholder = { Text("Chat room name") }
            )
        }

        item {
            if (suggestedGroups.isNotEmpty()) {
                Column(
                    modifier = Modifier.padding(top = 32.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Suggested chat rooms",
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.align(Alignment.Start)
                    )
                    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                        Column {
                            suggestedGroups.take(5)
                                .forEachIndexed { index, group ->
                                    Text(
                                        text = group.friendlyName,
                                        modifier = Modifier
                                            .clickable {
                                                chatRoomName = group.friendlyName
                                            }
                                            .padding(16.dp)
                                            .fillMaxWidth(),
                                    )
                                    if (index < suggestedGroups.size - 1) HorizontalDivider()
                                }
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
        )
    }
}