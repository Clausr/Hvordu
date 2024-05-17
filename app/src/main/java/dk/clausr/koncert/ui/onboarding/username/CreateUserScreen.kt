package dk.clausr.koncert.ui.onboarding.username

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dk.clausr.koncert.R
import dk.clausr.koncert.ui.compose.theme.KoncertTheme

@Composable
fun CreateUserRoute(
    modifier: Modifier = Modifier,
    viewModel: CreateUserViewModel = hiltViewModel(),
) {

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

            Button(onClick = {
                onCreateClicked(username)
            }) {
                Text("Go to chatroom")
            }
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