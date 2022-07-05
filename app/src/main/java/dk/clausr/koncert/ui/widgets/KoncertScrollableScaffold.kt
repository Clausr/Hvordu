package dk.clausr.koncert.ui.widgets

import androidx.annotation.StringRes
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dk.clausr.koncert.R
import dk.clausr.koncert.ui.compose.preview.ColorSchemeProvider
import dk.clausr.koncert.ui.compose.theme.KoncertTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KoncertScrollableScaffold(
    @StringRes titleRes: Int,
    content: LazyListScope.() -> Unit
) {
    val lazyListState = rememberLazyListState()
    val systemUiController = rememberSystemUiController()
    val hasScrolled by remember {
        derivedStateOf {
            lazyListState.firstVisibleItemScrollOffset > 0
        }
    }

    val appBarElevation by animateDpAsState(
        targetValue = if (hasScrolled) AppBarDefaults.TopAppBarElevation else 0.dp,
        animationSpec = spring(stiffness = Spring.StiffnessMedium)
    )
    systemUiController.setStatusBarColor(color = MaterialTheme.colorScheme.surfaceColorAtElevation(appBarElevation))

    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = MaterialTheme.colorScheme.surfaceColorAtElevation(appBarElevation),
                elevation = appBarElevation,
                title = {
                    Text(
                        text = stringResource(id = titleRes),
                        style = MaterialTheme.typography.headlineSmall
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
        KoncertScrollableScaffold(titleRes = R.string.app_name) {

        }
    }
}