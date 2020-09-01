package id.adeds.dynamic_ui.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.adeds.dynamic_ui.util.MyRequestState
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

open class BaseViewModel : ViewModel(), CoroutineScope {

    private val viewModelJob = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + viewModelJob

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    protected fun launch(action: suspend CoroutineScope.() -> Unit):
            Job = CoroutineScope(coroutineContext).launch { action(this) }

    protected suspend fun <T> asyncAwait(
        context: CoroutineContext = Dispatchers.IO,
        action: suspend CoroutineScope.() -> T
    ): T =
        withContext(CoroutineScope(context).coroutineContext) { action(this) }

    fun MutableLiveData<MyRequestState>.loading() {
        value = MyRequestState.Loading
    }

    fun MutableLiveData<MyRequestState>.success() {
        value = MyRequestState.Success
    }

    fun MutableLiveData<MyRequestState>.empty(emptyMsg: String?) {
        value = MyRequestState.Empty(emptyMsg)
    }

    fun MutableLiveData<MyRequestState>.failed(errorMsg: String?) {
        value = MyRequestState.Failed(errorMsg)
    }

}