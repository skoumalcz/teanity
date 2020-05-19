package com.skoumal.teanity.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import androidx.recyclerview.widget.AsyncDifferConfig
import com.skoumal.teanity.extensions.comparableCallback
import com.skoumal.teanity.util.ComparableEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

abstract class CoroutineBoundaryCallback<Item : ComparableEntity<Item>>(
    private val scope: CoroutineScope,
    private val dispatcher: CoroutineContext = EmptyCoroutineContext
) : PagedList.BoundaryCallback<Item>(), CoroutineScope by scope {

    abstract suspend fun onItemAtStartLoadRequested()
    abstract suspend fun onItemAtEndLoadRequested(item: Item)

    private val internalIsLoading = MutableLiveData(false)

    val isLoading: LiveData<Boolean> get() = internalIsLoading
    val asyncDifferConfig = AsyncDifferConfig.Builder(comparableCallback<Item>()).build()

    private var actingJob: Job? = null
        @Synchronized set(value) {
            if (field?.isActive == true || value == null) {
                return
            }
            field = value
        }

    override fun onZeroItemsLoaded() {
        actingJob = scheduleAsync { onItemAtStartLoadRequested() }
    }

    override fun onItemAtEndLoaded(itemAtEnd: Item) {
        actingJob = scheduleAsync { onItemAtEndLoadRequested(itemAtEnd) }
    }

    override fun onItemAtFrontLoaded(itemAtFront: Item) {
        actingJob = scheduleAsync { onItemAtStartLoadRequested() }
    }

    private inline fun scheduleAsync(crossinline body: suspend CoroutineScope.() -> Unit): Job? {
        val currentJob = actingJob
        if (currentJob != null && currentJob.isActive) {
            return null
        }
        internalIsLoading.postValue(true)
        return launch(dispatcher) { body() }.also {
            it.invokeOnCompletion { internalIsLoading.postValue(false) }
        }
    }

}
