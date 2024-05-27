package dk.clausr.hvordu.ui.compose.preview

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import dk.clausr.hvordu.ui.compose.theme.DarkColors
import dk.clausr.hvordu.ui.compose.theme.LightColors

class ColorSchemeProvider : PreviewParameterProvider<ColorScheme> {
    override val values: Sequence<ColorScheme>
        get() = sequenceOf(
            DarkColors,
            LightColors
        )
}
