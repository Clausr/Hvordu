package dk.clausr.koncert.utils.extensions

import androidx.annotation.Size

fun <T> List<T>.repeat(repeatBy: Int): List<T> {
    val resultList = this.toMutableList()
    for (i in 0 until repeatBy) {
        resultList += this
    }
    return resultList
}

@Size(min = 0)
inline fun <reified T> List<T>.repeatToSize(
    size: Int
): List<T> {
    if (isEmpty() || size <= 0) {
        return listOf()
    }
    val timesToRepeate = size / count()
    val missingItemsToCopy = size % count()
    val resultList = this.repeat(timesToRepeate - 1)
    return resultList + this.subList(0, missingItemsToCopy)
}