package dk.clausr.koncert.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dk.clausr.core.models.UserData
import dk.clausr.koncert.R
import dk.clausr.koncert.ui.compose.preview.ColorSchemeProvider
import dk.clausr.koncert.ui.compose.theme.KoncertTheme
import dk.clausr.repo.domain.Group

@Composable
fun HomeRoute(
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
    onNavigateToChat: (chatName: String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val groups = emptyList<Group>()

    when (val state = uiState) {
        HomeUiState.Loading -> Box(Modifier.fillMaxSize()) {
            CircularProgressIndicator()
        }

        is HomeUiState.Shown -> {
//            if (state.userData == null) {
            CreateUserScreen(
                windowSizeClass = windowSizeClass,
                userData = state.userData,
                modifier = modifier,
                onCreateClicked = {
                    viewModel.setData(it.username, it.group)
                    onNavigateToChat(it.group)
                },
                groups = groups,
            )
//            } else {
//                onNavigateToChat(state.userData.group)
//            }
        }
    }

//    if (userData == null) {
//        CreateUserScreen(
//            windowSizeClass = windowSizeClass,
//            userData = null,
//            modifier = modifier,
//            onCreateClicked = {
//                Timber.d("Set data: $it")
//                viewModel.setData(it.username, it.group)
//            },
//            groups = groups,
//        )
//    } else {
//        //Chat screen
//        ChatRoute()
//    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateUserScreen(
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
    userData: UserData?,
    groups: List<Group>,
    onCreateClicked: (UserData) -> Unit,
) {
    var username by remember(userData) {
        mutableStateOf(userData?.username ?: "")
    }
    var group by remember(userData) {
        mutableStateOf(userData?.group ?: "")
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets.statusBars,
                title = {
                    Text(
                        text = stringResource(id = R.string.tab_overview),
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
                value = username,
                onValueChange = {
                    username = it
                },
                supportingText = { Text("Should be unique") },
                placeholder = { Text("Username") }
            )

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = group,
                onValueChange = {
                    group = it
                },
                supportingText = { Text("Create or join group of this name") },
                placeholder = { Text("Groupname") }
            )

            Button(onClick = {
                onCreateClicked(UserData(username = username, group = group, keyboardHeight = null))
            }) {
                Text("Go to chatroom")
            }

            Column {
                groups.forEach {
                    Text(
                        text = it.friendlyName,
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                            .clickable {
                                group = it.friendlyName
                            },
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(name = "phone", device = "spec:shape=Normal,width=360,height=640,unit=dp,dpi=480")
@Composable
fun Preview0(
    @PreviewParameter(ColorSchemeProvider::class) colorScheme: ColorScheme
) {
    BoxWithConstraints {
        KoncertTheme(overrideColorScheme = colorScheme) {
            CreateUserScreen(
                windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(maxWidth, maxHeight)),
                userData = UserData("Name", "Group", null),
                onCreateClicked = {},
                groups = listOf(
                    Group("", "Friendly name"),
                    Group("", "Another friendly name"),
                ),
            )
        }
    }
}