package dk.clausr.koncert.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Button
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dk.clausr.core.models.UserData
import dk.clausr.koncert.R
import dk.clausr.koncert.ui.compose.preview.ColorSchemeProvider
import dk.clausr.koncert.ui.compose.theme.KoncertTheme

@Composable
fun HomeRoute(
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val userData by viewModel.userData.collectAsStateWithLifecycle()

    if (userData == null) {
        CreateUserScreen(
            windowSizeClass = windowSizeClass,
            userData = userData,
            modifier = modifier,
            onCreateClicked = { viewModel.setData(it.username, it.group) }
        )
    } else {
        //Chat screen
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateUserScreen(
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
    userData: UserData?,
    onCreateClicked: (UserData) -> Unit,
) {
    var username by remember(userData) {
        mutableStateOf(userData?.username ?: "")
    }
    var group by remember(userData) {
        mutableStateOf(userData?.group ?: "")
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets.statusBars,
                colors = TopAppBarDefaults.topAppBarColors(),
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
                onCreateClicked(UserData(username, group))
            }) {
                Text("Start")
            }
        }
    }
}


@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(name = "phone", device = "spec:shape=Normal,width=360,height=640,unit=dp,dpi=480")
@Preview(name = "landscape", device = "spec:shape=Normal,width=640,height=360,unit=dp,dpi=480")
@Preview(name = "tablet", device = "spec:shape=Normal,width=1280,height=800,unit=dp,dpi=480")
@Composable
fun Preview0(
    @PreviewParameter(ColorSchemeProvider::class) colorScheme: ColorScheme
) {
    BoxWithConstraints {
        KoncertTheme {
            CreateUserScreen(
                windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(maxWidth, maxHeight)),
                userData = UserData("Name", "Group"),
                onCreateClicked = {},
            )
        }
    }
}