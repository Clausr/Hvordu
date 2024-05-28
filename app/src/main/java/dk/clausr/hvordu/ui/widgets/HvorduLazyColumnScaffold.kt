package dk.clausr.hvordu.ui.widgets

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import dk.clausr.hvordu.ui.compose.preview.ColorSchemeProvider
import dk.clausr.hvordu.ui.compose.theme.HvorduTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HvorduLazyColumnScaffold(
    title: String,
    modifier: Modifier = Modifier,
    lazyColumnContentPadding: PaddingValues = PaddingValues(),
    bottomBarContent: @Composable () -> Unit = {},
    content: LazyListScope.() -> Unit,
) {
    val lazyListState = rememberLazyListState()

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets.statusBars,
                colors = TopAppBarDefaults.topAppBarColors(),
                title = {
                    Text(
                        text = title,
                    )
                },
            )
        },
        bottomBar = bottomBarContent,
    ) { innerPadding ->
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(innerPadding),
            state = lazyListState,
            contentPadding = lazyColumnContentPadding,
            content = content
        )
    }
}

@Preview
@Composable
private fun Preview(
    @PreviewParameter(ColorSchemeProvider::class) colorScheme: ColorScheme
) {
    HvorduTheme(overrideColorScheme = colorScheme) {
        HvorduLazyColumnScaffold(
            title = "R.string.app_name",
        ) {

        }
    }
}