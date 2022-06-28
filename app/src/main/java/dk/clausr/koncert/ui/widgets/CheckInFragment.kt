package dk.clausr.koncert.ui.widgets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import dk.clausr.koncert.databinding.FragmentCheckInBinding
import dk.clausr.koncert.ui.compose.preview.PreviewColorPaletteProviderLightDark
import dk.clausr.koncert.ui.compose.theme.KoncertColors
import dk.clausr.koncert.ui.compose.theme.KoncertTheme
import dk.clausr.koncert.utils.extensions.setKoncertContent

@AndroidEntryPoint
class CheckInFragment : Fragment() {

    lateinit var binding: FragmentCheckInBinding

    private val vm by viewModels<TestViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCheckInBinding.inflate(inflater, container, false)

        binding.composeView.setKoncertContent {
//            KoncertTheme {
//                val textState = vm.testFlow.collectAsState(initial = "")
//                Scaffold(modifier = Modifier.padding(16.dp)) {
//                    ArtistName(textState.value)
//                }
//            }
        }

        return binding.root
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

    @Preview(name = "Checkin")
    @Composable
    fun Preview() {
        KoncertTheme {
            ArtistName(initValue = "Fisk")
        }
    }

    @Preview(name = "Checkin Dark")
    @Composable
    fun PreviewDark(
        @PreviewParameter(PreviewColorPaletteProviderLightDark::class) palette: KoncertColors
    ) {
        KoncertTheme(colorPalette = palette) {
            ArtistName(initValue = "Fisk")
        }
    }
}