package dk.clausr.hvordu.ui.onboarding.username

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.safeDrawing
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import dk.clausr.hvordu.R
import dk.clausr.hvordu.ui.compose.theme.HvorduTheme
import dk.clausr.hvordu.ui.onboarding.navigation.JOIN_CHAT_ROOM_ROUTE
import dk.clausr.hvordu.utils.extensions.collectWithLifecycle

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

    val username by viewModel.username.collectAsState(initial = null)

    CreateUserScreen(
        modifier = modifier,
        existingUsername = username,
        onCreateClicked = {
            viewModel.setUsername(it)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateUserScreen(
    modifier: Modifier = Modifier,
    existingUsername: String? = null,
    onCreateClicked: (username: String) -> Unit,
) {
    var username by remember(existingUsername) {
        mutableStateOf(TextFieldValue(existingUsername ?: "", selection = TextRange(Int.MAX_VALUE)))
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
                        text = stringResource(id = R.string.create_username_title),
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
                    .safeContentPadding(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    enabled = username.text.isNotBlank(),
                    onClick = {
                        onCreateClicked(username.text)
                    },
                ) {
                    Text(stringResource(id = R.string.general_continue))
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
                    keyboardType = KeyboardType.Email,
                    capitalization = KeyboardCapitalization.None,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { onCreateClicked(username.text) }),
                supportingText = { Text(stringResource(id = R.string.create_username_text_field_supporting_text)) },
                placeholder = { Text(stringResource(id = R.string.create_username_text_field_placeholder)) }
            )
        }
    }
}

@Composable
@Preview
private fun PreviewEmptyScreen() {
    HvorduTheme {
        CreateUserScreen(
            onCreateClicked = {},
        )
    }
}