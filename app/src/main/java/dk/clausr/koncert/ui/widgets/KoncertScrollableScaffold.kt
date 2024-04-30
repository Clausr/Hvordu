package dk.clausr.koncert.ui.widgets

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import dk.clausr.koncert.R
import dk.clausr.koncert.ui.compose.preview.ColorSchemeProvider
import dk.clausr.koncert.ui.compose.theme.KoncertTheme
import dk.clausr.koncert.utils.extensions.isScrollingUp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KoncertScrollableScaffold(
    @StringRes titleRes: Int,
    floatingActionButton: @Composable () -> Unit = {},
    content: LazyListScope.() -> Unit
) {
    val lazyListState = rememberLazyListState()

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        floatingActionButton = {
            ExtendableFloatingActionButton(
                extended = lazyListState.isScrollingUp(),
                text = { Text(text = "Check in") },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )
                }
            )
        },
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets.statusBars,
                colors = TopAppBarDefaults.topAppBarColors(),
//                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(state = rememberTopAppBarState()),
                title = {
                    Text(
                        text = stringResource(id = titleRes),
                    )
                },
            )
        },
    ) { innerPadding ->
        LazyColumn(
            Modifier
                .fillMaxSize(),
            state = lazyListState,
            contentPadding = innerPadding,
            content = content
        )
    }
}

@Preview
@Composable
private fun Preview(
    @PreviewParameter(ColorSchemeProvider::class) colorScheme: ColorScheme
) {
    KoncertTheme(overrideColorScheme = colorScheme) {
        KoncertScrollableScaffold(
            titleRes = R.string.app_name
        ) {

        }
    }
}