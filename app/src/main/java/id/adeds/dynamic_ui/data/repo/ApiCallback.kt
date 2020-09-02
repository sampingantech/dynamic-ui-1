package id.adeds.dynamic_ui.data.repo

interface ApiCallback<T> {
    fun onSuccess(data: T)
    fun onFailed(e: Throwable)
}