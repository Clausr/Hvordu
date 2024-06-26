package dk.clausr.hvordu.ui.home.overview

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import dk.clausr.core.extensions.getCustomRelativeTimeSpanString
import dk.clausr.hvordu.R
import dk.clausr.hvordu.repo.domain.ChatRoom
import dk.clausr.hvordu.ui.compose.theme.HvorduTheme
import kotlinx.datetime.Clock

@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    onNavigateToChat: (chatRoomId: String, chatName: String?) -> Unit,
    addNewRoom: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    when (val state = uiState) {
        HomeUiState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }

        is HomeUiState.Shown -> {
            HomeScreen(
                chatRooms = state.chatRooms,
                modifier = modifier.fillMaxSize(),
                onChatRoomClicked = onNavigateToChat,
                addNewRoom = addNewRoom,
                onSignOut = { viewModel.signOut() },
            )
        }

        HomeUiState.Unauthenticated -> {
            HomeEmptyState(
                onCta = viewModel::signInWithGoogle
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    chatRooms: List<ChatRoom>,
    onChatRoomClicked: (chatRoomId: String, chatName: String?) -> Unit,
    addNewRoom: () -> Unit,
    onSignOut: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) },
                actions = {
                    IconButton(onClick = { onSignOut() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.Logout,
                            contentDescription = null
                        )
                    }
                })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { addNewRoom() }) {
                Icon(Icons.Outlined.Add, null)
            }
        }
    ) { innerPadding ->
        if (chatRooms.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = innerPadding.calculateTopPadding())
            ) {
                if (chatRooms.isNotEmpty()) {
                    items(chatRooms) { chatRoom ->
                        Row(
                            modifier = Modifier
                                .clickable { onChatRoomClicked(chatRoom.id, chatRoom.roomName) }
                                .padding(16.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(
                                    text = chatRoom.roomName,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
                                )
                                Text(
                                    text = "${chatRoom.sender}: ${chatRoom.latestMessage} • ${chatRoom.latestMessageAt?.getCustomRelativeTimeSpanString()}",
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                )
                            }

                            Spacer(Modifier.weight(1f))
                            AsyncImage(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(60.dp),
                                contentScale = ContentScale.Crop,
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(chatRoom.imageUrl)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = null,
                            )
                        }
                    }
                }
            }
        }
    }
}


@Preview
@Composable
private fun HomeScreenPreview() {
    HvorduTheme {
        val chatRooms = List(10) {
            ChatRoom(
                id = "",
                roomName = "Chat room ",
                latestMessage = "Seneste besked".repeat(it),
                latestMessageAt = Clock.System.now(),
                sender = "Some sender",
                imageUrl = null,
            )
        }
        HomeScreen(
            chatRooms = chatRooms,
            onChatRoomClicked = { _, _ -> },
            addNewRoom = {},
            onSignOut = {},
        )
    }
}