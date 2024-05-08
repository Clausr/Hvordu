package dk.clausr.koncert.ui.home.components

import androidx.compose.ui.graphics.Color

sealed class NowData(open val name: String) {
    data class Folder(override val name: String, val items: List<Element>) : NowData(name)
    data class Element(override val name: String, val color: Color) : NowData(name)
}
