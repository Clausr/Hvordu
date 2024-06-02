package dk.clausr.hvordu.ui.chat

import android.net.Uri
import androidx.camera.core.ImageCapture
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.currentStateAsState
import dk.clausr.hvordu.repo.domain.Message
import dk.clausr.hvordu.ui.chat.mapper.mapToChatItem
import dk.clausr.hvordu.ui.chat.ui.ChatItem
import dk.clausr.hvordu.utils.extensions.toDp
import dk.clausr.hvordu.utils.extensions.toPx
import io.github.jan.supabase.realtime.RealtimeChannel
import timber.log.Timber

@Composable
fun ChatRoute(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    chatViewModel: ChatViewModel = hiltViewModel(),
) {
    val messages by chatViewModel.messages.collectAsStateWithLifecycle()
    val status by chatViewModel.connectionStatus.collectAsStateWithLifecycle()
    val chatName by chatViewModel.chatName.collectAsState()
    val imageUri by chatViewModel.imageUri.collectAsStateWithLifecycle(null)

    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateAsState()

    LaunchedEffect(lifecycleState) {
        Timber.d("Lifecycle state $lifecycleState")
        when (lifecycleState) {
            Lifecycle.State.CREATED -> chatViewModel.connectToRealtime()
            else -> Unit
        }
    }

    LaunchedEffect(Unit) {
        chatViewModel.connectToRealtime()
    }

    ChatScreen(
        modifier = modifier,
        chatName = chatName ?: "",
        messages = messages,
        connectionStatus = status,
        onSendChat = {
            chatViewModel.sendMessage(it)
        },
        pictureResult = {
            chatViewModel.setImageUri(it)
        },
        imageUri = imageUri,
        removeImage = { chatViewModel.deleteImage() },
        onBack = onBack,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ChatScreen(
    chatName: String,
    messages: List<Message>,
    connectionStatus: RealtimeChannel.Status,
    onSendChat: (String) -> Unit,
    pictureResult: (Result<ImageCapture.OutputFileResults>) -> Unit,
    imageUri: Uri?,
    removeImage: (Uri) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val lazyState = rememberLazyListState()
    val snackbarHost = remember { SnackbarHostState() }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty() && !lazyState.isScrollInProgress) {
            lazyState.scrollToItem(0)
        }
    }

    val navigationInsetsHeight =
        WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding().toPx.toInt()
    var bottomBarHeight by remember { mutableIntStateOf(navigationInsetsHeight) }

    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHost)
        },
        topBar = {
            TopAppBar(
                title = { Text(chatName) },
                actions = {
                    val statusEmoji = when (connectionStatus) {
                        RealtimeChannel.Status.UNSUBSCRIBED -> "🔴"
                        RealtimeChannel.Status.SUBSCRIBING -> "🟡"
                        RealtimeChannel.Status.SUBSCRIBED -> "🟢"
                        RealtimeChannel.Status.UNSUBSCRIBING -> "🟣"
                    }
                    IconButton(onClick = {}) {
                        Text(statusEmoji)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        },
        bottomBar = {
            ChatComposer(
                modifier = Modifier.onSizeChanged { bottomBarHeight = it.height },
                onChatSent = onSendChat,
                pictureResult = pictureResult,
                imageUri = imageUri,
                removeImage = { removeImage(it) },
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
        Box(
            modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding()),
        ) {
            LazyColumn(
                state = lazyState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(bottom = bottomBarHeight.toDp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                reverseLayout = true,
            ) {
                items(messages.reversed()) { message ->
                    val chatItem = message.mapToChatItem()
                    ChatItem(
                        item = chatItem,
                        timestamp = message.createdAt,
                    )
                }
            }
        }
    }
}