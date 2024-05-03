package dk.clausr.core.utils

sealed class DataState<out T> {
    object Loading : DataState<Nothing>()
    data class Success<out R>(val value: R) : DataState<R>()
    data class Failure(val message: String?, val throwable: Throwable?) : DataState<Nothing>()
}
