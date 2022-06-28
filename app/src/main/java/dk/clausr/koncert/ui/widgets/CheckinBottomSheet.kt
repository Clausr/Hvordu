package dk.clausr.koncert.ui.widgets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dk.clausr.koncert.ui.compose.theme.KoncertTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CheckinBottomSheet(

) {
    val bottomState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    ModalBottomSheetLayout(
        sheetState = bottomState,
        sheetShape = MaterialTheme.shapes.large,
        sheetContent = {

        }
    ) {
        KoncertTheme {
            Scaffold(
                topBar = {
                    TopAppBar(modifier = Modifier.fillMaxWidth()) {
                        Text(text = "Check in")
                    }
                },
                content = {
//                    Column(modifier = Modifier.fillMaxWidth()) {
//
//                    }
                    ArtistName(initValue = "Fisk")

                },
            )
        }
    }
}

@Composable
fun ArtistName(initValue: String) {
    var text by rememberSaveable { mutableStateOf(initValue) }

    val onValueChanged = { newText: String ->
        text = newText
    }

    TextField(
        value = text,
        onValueChange = onValueChanged,
        label = { Text("Artist") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
}