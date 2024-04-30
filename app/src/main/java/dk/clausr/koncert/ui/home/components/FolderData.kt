package dk.clausr.koncert.ui.home.components

import androidx.compose.ui.graphics.Color

data class FolderData(
    val items: List<FolderItem>,
    val name: String,
)

data class FolderItem(
    val name: String,
    val color: Color,
)