package dk.clausr.koncert.ui.chat

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dk.clausr.repo.domain.Message
import io.github.jan.supabase.realtime.RealtimeChannel

@Composable
fun ChatRoute(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    chatViewModel: ChatViewModel = hiltViewModel(),
) {
    BackHandler {
        onBack()
        chatViewModel.disconnectFromRealtime()
    }
    val messages by chatViewModel.messages.collectAsStateWithLifecycle()
    val status by chatViewModel.connectionStatus.collectAsStateWithLifecycle()

    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        chatViewModel.getMessages()
        chatViewModel.connectToRealtime()
    }

    ChatScreen(
        modifier = modifier,
        messages = messages,
        connectionStatus = status,
        onBack = {
            onBack()
            chatViewModel.disconnectFromRealtime()
        },
        onSendChat = {
            chatViewModel.sendMessage(it)
            keyboardController?.hide()
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    messages: List<Message>,
    connectionStatus: RealtimeChannel.Status,
    onSendChat: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val lazyState = rememberLazyListState()

    LaunchedEffect(messages.lastOrNull()?.id) {
        if (messages.isNotEmpty() && !lazyState.isScrollInProgress) {
            lazyState.scrollToItem(0)
        }
    }
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Chad") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                },
                actions = {
                    val statusEmoji = when (connectionStatus) {
                        RealtimeChannel.Status.UNSUBSCRIBED -> "🔴"
                        RealtimeChannel.Status.SUBSCRIBING -> "🟡"
                        RealtimeChannel.Status.SUBSCRIBED -> "🟢"
                        RealtimeChannel.Status.UNSUBSCRIBING -> "🟣"
                    }
                    Text(statusEmoji)
                }
            )
        },
        bottomBar = {
            ChatComposer(
                onChatSent = onSendChat,
            )
        }
    ) { innerPadding ->
        LazyColumn(
            state = lazyState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            reverseLayout = true,
        ) {
            items(messages) { message ->
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Card {
                        Column(Modifier.padding(8.dp)) {
                            Text(text = message.creatorId, fontWeight = FontWeight.Bold)
                            Text(message.content)
                        }
                    }
                    Text(message.createdAt)
                }
            }
        }
    }
}