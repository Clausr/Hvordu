package dk.clausr.hvordu.utils.extensions

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle


fun AnnotatedString.color(color: Color): AnnotatedString {
    return buildAnnotatedString {
        withStyle(style = SpanStyle(color = color)) {
            append(this@color)
        }
    }
}

val AnnotatedString.bold: AnnotatedString
    get() = this.let {
        buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append(it)
            }
        }
    }