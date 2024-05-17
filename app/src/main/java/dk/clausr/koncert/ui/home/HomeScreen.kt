package dk.clausr.koncert.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dk.clausr.koncert.utils.extensions.collectWithLifecycle
import dk.clausr.repo.domain.Group

@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    onNavigateToChat: (chatName: String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    viewModel.viewEffects.collectWithLifecycle {
        when (it) {
            is HomeViewModel.HomeViewEffect.OpenChat -> onNavigateToChat(it.roomId)
        }
    }

    when (val state = uiState) {
        HomeUiState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }

        is HomeUiState.Shown -> {
            HomeScreen(
                chatRooms = state.chatRooms,
                modifier = modifier.fillMaxSize(),
                onChatRoomClicked = onNavigateToChat,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    chatRooms: List<Group>,
    onChatRoomClicked: (chatRoomId: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(title = { Text(text = "Chad") })
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
        ) {
            if (chatRooms.isNotEmpty()) {
                items(chatRooms) { chatRoom ->
                    Row(
                        modifier = Modifier
                            .clickable { onChatRoomClicked(chatRoom.id) }
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(chatRoom.friendlyName, style = MaterialTheme.typography.labelLarge)
                            Text("Maybe the last text from the chatroom?")
                        }
                    }
                }
            } else {
                item { Text("Empty state :(") }
            }
        }
    }
}