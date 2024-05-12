package dk.clausr.koncert.ui.home

import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dk.clausr.core.models.UserData
import dk.clausr.koncert.R
import dk.clausr.koncert.ui.chat.ChatRoute
import dk.clausr.koncert.ui.compose.preview.ColorSchemeProvider
import dk.clausr.koncert.ui.compose.theme.KoncertTheme
import timber.log.Timber

@Composable
fun HomeRoute(
    windowSizeClass: WindowSizeClass,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val userData by viewModel.userData.collectAsStateWithLifecycle()

    if (userData == null) {
        CreateUserScreen(
            windowSizeClass = windowSizeClass,
            userData = null,
            modifier = modifier,
            onCreateClicked = {
                Timber.d("Set data: $it")
                viewModel.setData(it.username, it.group)
            }
        )
    } else {
        //Chat screen
        ChatRoute()
    }
}

@Composable
fun UserScreen(
    userData: UserData,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.background
        ) { innerPadding ->
            Column(
                modifier = modifier
                    .padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = "Username: ${userData.username}")
                Text(text = "Group: ${userData.group}")

                Button(onClick = onClick) {
                    Text(text = "Click me")
                }
            }
        }
    }
}

@Composable
fun Scrim(
    color: Color,
    onDismissRequest: () -> Unit,
    visible: Boolean
) {
    if (color.isSpecified) {
        val alpha by animateFloatAsState(
            targetValue = if (visible) 1f else 0f,
            animationSpec = TweenSpec()
        )
        val dismissSheet = if (visible) {
            Modifier
                .pointerInput(onDismissRequest) {
                    detectTapGestures {
                        onDismissRequest()
                    }
                }
                .clearAndSetSemantics {}
        } else {
            Modifier
        }
        Canvas(
            Modifier
                .zIndex(500F)
                .fillMaxSize()
                .then(dismissSheet)
        ) {
            drawRect(color = color, alpha = alpha)
        }
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
        modifier = modifier,
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
                onCreateClicked(UserData(username = username, group = group, keyboardHeight = null))
            }) {
                Text("Start")
            }
        }
    }
}

//@Preview
//@Composable
//fun UserScreenPreview() {
//    KoncertTheme {
//        UserScreen(userData = UserData("Name", "group", null), {})
//    }
//}

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
            )
        }
    }
}