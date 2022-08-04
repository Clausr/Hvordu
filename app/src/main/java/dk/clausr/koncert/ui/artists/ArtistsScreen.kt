package dk.clausr.koncert.ui.artists

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dk.clausr.koncert.R
import dk.clausr.koncert.ui.widgets.KoncertScrollableScaffold

@Composable
fun ArtistsRoute(
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
) {
    ArtistsScreen(
        windowSizeClass = windowSizeClass,
        modifier = modifier
    )
}

@Composable
fun ArtistsScreen(
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
) {
    KoncertScrollableScaffold(
        titleRes = R.string.tab_artists,
        content = {
            item {
                Text(text = "Lelele", style = MaterialTheme.typography.headlineSmall)
            }
        }
    )
}