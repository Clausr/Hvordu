package dk.clausr.koncert.ui.home

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.hilt.navigation.compose.hiltViewModel
import dk.clausr.core.models.Concert
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

@Composable
fun AllConcerts(
    concertList: List<Concert>
) {
//    val concertList = homeViewModel.concerts.collectAsState(initial = listOf())
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
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

@Preview(name = "AllConcerts preview")
@Composable
fun Preview0(
    @PreviewParameter(ColorSchemeProvider::class) colorScheme: ColorScheme
) {
    KoncertTheme(overrideColorScheme = colorScheme) {
        AllConcerts(concertList = ConcertMocks.concertsMock)
    }
}