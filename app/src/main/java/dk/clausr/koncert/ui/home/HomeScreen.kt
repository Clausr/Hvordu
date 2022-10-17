@file:OptIn(ExperimentalLifecycleComposeApi::class)

package dk.clausr.koncert.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
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
import dk.clausr.repo.concerts.ConcertMocks

@Composable
fun HomeRoute(
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val concertsState = viewModel.concerts.collectAsStateWithLifecycle()
    HomeScreen(
        windowSizeClass = windowSizeClass,
        concertState = concertsState.value,
        modifier = modifier
    )
}

@Composable
fun HomeScreen(
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
    concertState: List<Concert>
) {
    AllConcerts(
        concertList = concertState,
    )
}

@Composable
fun AllConcerts(
    concertList: List<Concert>
) {
    KoncertScrollableScaffold(
        titleRes = R.string.app_name,
        floatingActionButton = {
//            ExtendableFloatingActionButton(extended = fabExtended, text = { /*TODO*/ }) {
//
//            }
        }

    ) {
        item {
//            MostRecentConcerts(concertList)
            Spacer(modifier = Modifier.height(KoncertTheme.dimensions.padding8))

        }

        items(items = concertList) { concert ->
            MostRecentCard(
                concert = concert,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = KoncertTheme.dimensions.padding16),
                onClick = {}
            )
            Spacer(modifier = Modifier.height(KoncertTheme.dimensions.padding8))
        }

        (1..100).map {

            item {
                Text(
                    text = "Bla bla",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = KoncertTheme.dimensions.padding16, vertical = KoncertTheme.dimensions.padding8)
                )
            }
        }

        item {
            Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
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
                concertState = ConcertMocks.concertsMock,
            )
        }
    }
}