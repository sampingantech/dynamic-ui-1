package id.adeds.dynamic_ui.util

sealed class MyResult<out T : Any> {
    data class Success<out T : Any>(val data: T) : MyResult<T>()
    data class Loading(val status: Boolean) : MyResult<Nothing>()
    sealed class Error(val exception: Exception) : MyResult<Nothing>() {
        class RecoverableError(exception: Exception) : Error(exception)
        class NonRecoverableError(exception: Exception) : Error(exception)
    }
}