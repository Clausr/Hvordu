package dk.clausr.koncert.ui.chat

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dk.clausr.koncert.ui.chat.mapper.mapToChatItem
import dk.clausr.koncert.ui.chat.ui.ChatItem
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

    LaunchedEffect(Unit) {
        chatViewModel.getMessages()
        chatViewModel.connectToRealtime()
    }

    ChatScreen(
        modifier = modifier,
        chatName = chatViewModel.topic,
        messages = messages,
        connectionStatus = status,
        onBack = {
            onBack()
            chatViewModel.disconnectFromRealtime()
        },
        onSendChat = {
            chatViewModel.sendMessage(it)
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ChatScreen(
    chatName: String,
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
        contentWindowInsets = WindowInsets(0),
        modifier = modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(chatName) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Go back")
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
        },
        containerColor = MaterialTheme.colorScheme.background,
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
                val chatItem = message.mapToChatItem()

                ChatItem(
                    modifier = Modifier.animateItemPlacement(),
                    item = chatItem,
                    timestamp = message.createdAt,
                )
            }
        }
    }
}