package dk.clausr.koncert.ui.onboarding.username

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import dk.clausr.koncert.R
import dk.clausr.koncert.ui.compose.theme.KoncertTheme
import dk.clausr.koncert.ui.onboarding.navigation.JOIN_CHAT_ROOM_ROUTE
import dk.clausr.koncert.utils.extensions.collectWithLifecycle

@Composable
fun CreateUserRoute(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: CreateUserViewModel = hiltViewModel(),
) {

    viewModel.viewEffects.collectWithLifecycle {
        when (it) {
            CreateUserViewEffect.NavigateToJoinChatRoom -> navController.navigate(
                JOIN_CHAT_ROOM_ROUTE
            )
        }
    }

    CreateUserScreen(
        modifier = modifier,
        onCreateClicked = viewModel::setUsername
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateUserScreen(
    modifier: Modifier = Modifier,
    onCreateClicked: (username: String) -> Unit,
) {
    var username by remember {
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
                        text = stringResource(id = R.string.tab_overview),
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
                    enabled = username.isNotBlank(),
                    onClick = {
                        onCreateClicked(username)
                    },
                ) {
                    Text("Continue")
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
                value = username,
                maxLines = 1,
                onValueChange = {
                    username = it
                },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { onCreateClicked(username) }),
                supportingText = { Text("Should be unique") },
                placeholder = { Text("Username") }
            )


        }
    }
}

@Composable
@Preview
private fun PreviewEmptyScreen() {
    KoncertTheme {
        CreateUserScreen(
            onCreateClicked = {},
        )
    }
}