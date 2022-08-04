@file:OptIn(ExperimentalLifecycleComposeApi::class)

package dk.clausr.koncert.ui.home

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.DpSize
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dk.clausr.core.models.Concert
import dk.clausr.koncert.R
import dk.clausr.koncert.ui.compose.preview.ColorSchemeProvider
import dk.clausr.koncert.ui.compose.theme.KoncertTheme
import dk.clausr.koncert.ui.widgets.KoncertScrollableScaffold
import dk.clausr.koncert.utils.extensions.repeatToSize
import dk.clausr.repo.concerts.ConcertMocks
import timber.log.Timber

@Composable
fun HomeRoute(
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val concertsState = viewModel.concerts.collectAsStateWithLifecycle()
    HomeScreen(
        windowSizeClass = windowSizeClass,
        concertState = concertsState,

        modifier = modifier
    )
}

@Composable
fun HomeScreen(
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
    concertState: State<List<Concert>>
) {
    AllConcerts(
        concertList = concertState.value.repeatToSize(100),
    )
}

@Composable
fun AllConcerts(
    concertList: List<Concert>
) {
    KoncertScrollableScaffold(titleRes = R.string.app_name) {
        items(items = concertList) { concert ->
            ConcertCard(
                artistName = concert.artist.name,
                venueName = concert.venue.name,
                onClick = { Timber.d("Clicked on ${concert}") }
            )
        }
    }
}


@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(name = "phone", device = "spec:shape=Normal,width=360,height=640,unit=dp,dpi=480")
@Preview(name = "landscape", device = "spec:shape=Normal,width=640,height=360,unit=dp,dpi=480")
//@Preview(name = "foldable", device = "spec:shape=Normal,width=673,height=841,unit=dp,dpi=480")
@Preview(name = "tablet", device = "spec:shape=Normal,width=1280,height=800,unit=dp,dpi=480")
@Composable
fun Preview0(
    @PreviewParameter(ColorSchemeProvider::class) colorScheme: ColorScheme
) {
    BoxWithConstraints {
        KoncertTheme(overrideColorScheme = colorScheme) {
            HomeScreen(
                windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(maxWidth, maxHeight)),
                concertState = mutableStateOf(ConcertMocks.concertsMock),
            )
        }
    }

}