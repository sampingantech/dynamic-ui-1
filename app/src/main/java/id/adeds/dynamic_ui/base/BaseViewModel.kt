package id.adeds.dynamic_ui.base

import androidx.lifecycle.ViewModel
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

}