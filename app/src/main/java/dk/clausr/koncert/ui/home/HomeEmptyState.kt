package dk.clausr.koncert.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeEmptyState(
    modifier: Modifier = Modifier,
    onCta: () -> Unit = {},
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Hey, hvordu?", style = MaterialTheme.typography.titleLarge)
        Text(text = "Log ind for at få adgang...")

        Button(onClick = onCta, modifier = Modifier.padding(top = 16.dp)) {
            Text("Log ind med Google")
        }
    }
}