package dk.clausr.koncert.ui.home

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.TopAppBar
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dk.clausr.core.models.Concert
import dk.clausr.koncert.R
import dk.clausr.koncert.ui.compose.preview.ColorSchemeProvider
import dk.clausr.koncert.ui.compose.theme.KoncertTheme
import dk.clausr.repo.concerts.ConcertMocks
import timber.log.Timber

@Composable
fun AllConcertsContainer(
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val concertList = homeViewModel.concerts.collectAsState(initial = listOf())
    AllConcerts(concertList = concertList.value)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllConcerts(
    concertList: List<Concert>
) {
//    val concertList = homeViewModel.concerts.collectAsState(initial = listOf())
    val lazyListState = rememberLazyListState()

    val hasScrolled by remember {
        derivedStateOf {
            lazyListState.firstVisibleItemScrollOffset > 0
        }
    }
    val appBarElevation by animateDpAsState(if (hasScrolled) 3.dp else 0.dp)


    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = MaterialTheme.colorScheme.surfaceColorAtElevation(appBarElevation),
                elevation = appBarElevation,
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        textAlign = TextAlign.Center
                    )
                },
            )
        },
    ) { innerPadding ->
        LazyColumn(
            state = lazyListState,
            contentPadding = innerPadding,
            verticalArrangement = Arrangement.spacedBy(KoncertTheme.dimensions.padding8)
        ) {
            items(concertList) { concert ->
                ConcertCard(
                    artistName = concert.artist.name,
                    venueName = concert.venue.name,
                    onClick = { Timber.d("Clicked on ${concert}") }
                )
            }
        }
    }

}

@Preview(name = "AllConcerts preview")
@Composable
fun Preview0(
    @PreviewParameter(ColorSchemeProvider::class) colorScheme: ColorScheme
) {
    KoncertTheme(overrideColorScheme = colorScheme) {
        AllConcerts(concertList = ConcertMocks.concertsMock)
    }
}