package com.skoumal.teanity.viewmodel

import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evernote.android.state.StateSaver
import com.skoumal.teanity.util.Insets
import com.skoumal.teanity.util.KObservableField
import com.skoumal.teanity.viewevents.SimpleViewEvent
import com.skoumal.teanity.viewevents.ViewEvent
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

abstract class TeanityViewModel : ViewModel(), CoroutineScope {

    private val disposables = CompositeDisposable()

    override val coroutineContext: CoroutineContext get() = viewModelScope.coroutineContext

    private val _viewEvents = PublishSubject.create<ViewEvent>()
    val viewEvents: Observable<ViewEvent> get() = _viewEvents

    val insets = KObservableField(Insets.empty)

    internal var lastRefresh = 0L
    private var currentJob: Job? = null
    private var currentHandle: DisposableHandle? = null

    protected open val minRefreshDelay = 0

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    /**
     * Never call this function directly unless you know what you're doing.
     * Instead you'd call [requestRefresh] which ensures refreshes never overlap.
     *
     * On top of that [refresh] is called on main thread within coroutine. Feel free to launch more coroutines,
     * this is however to trim unnecessary launch block.
     * */
    protected open suspend fun refresh() = Unit

    @CallSuper
    internal open suspend fun induceRefresh() = refresh()

    /**
     * Issues a request that will immediately invoke [induceRefresh] and consecutively [refresh] unless conditions
     * for refreshing are not met.
     *
     * 1) ### Only _one_ job can refresh data at once.
     *
     *      This rule is especially important if you're refreshing your list/recycler data which (in overlapping tasks)
     *      might encounter nasty inconsistencies. On contrary running overlapping refresh tasks is most likely
     *      an error caused by a developer.
     *
     * 2) ### Minimal refresh delay
     *
     *      Considering existence of manual refresh mechanisms this feels mandatory to include since users are users
     *      and they like to swipe to refresh repeatedly. Using override of [minRefreshDelay] you can adjust
     *      (or disable) the minimal delay.
     *
     *      By default [minRefreshDelay]=0, and can be enabled by setting it to >0. The [minRefreshDelay] is specified in millis.
     *
     * Please note that [refresh] is not guaranteed to run after issuing refresh request. This can be problematic for
     * when you've your UI logic (namely resetting) placed within [refresh] method and using a view that controls its
     * own state at the same time. This can be, however, easily overcame by checking for a result of this method which
     * implies whether or not what the refresh started. Whenever it returns true, there's a guarantee that [refresh]
     * will be called, if not you'd probably set that view to idle state manually.
     *
     * [induceRefresh] is used privately within this library to adjust behavior for different types of ViewModels.
     * For instance [LoadingViewModel] would set its state to [State.LOADING] if it wasn't ran before, ensuring that
     * user will see loader just whenever there's nothing to display. Moreover it will automatically set itself
     * to [State.LOADED] as it completes.
     * */
    @Synchronized
    fun requestRefresh(): Boolean {
        if (currentJob?.isActive == true) {
            currentHandle?.dispose()
            currentHandle = currentJob?.invokeOnCompletion { requestRefresh() }
            Log.i(
                javaClass.simpleName,
                "Data cannot be refreshed concurrently. Request will be automatically invoked once job has completed."
            )
            return false
        }

        if (SystemClock.uptimeMillis() - lastRefresh < minRefreshDelay) {
            Log.i(
                javaClass.simpleName,
                "Data cannot be refreshed at this time, minimal refresh delay haven't expired yet. Request to refresh is denied."
            )
            return false
        }

        lastRefresh = SystemClock.uptimeMillis()
        currentJob = launch {
            induceRefresh()
        }
        return true
    }

    fun restoreState(savedInstanceState: Bundle?) {
        StateSaver.restoreInstanceState(this, savedInstanceState)
    }

    fun saveState(outState: Bundle) {
        StateSaver.saveInstanceState(this, outState)
    }

    fun <Event : ViewEvent> Event.publish() {
        _viewEvents.onNext(this)
    }

    fun Int.publish() {
        _viewEvents.onNext(SimpleViewEvent(this))
    }

    fun Disposable.add() {
        disposables.add(this)
    }

    protected fun launch(
        context: CoroutineContext = coroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ) = (this as CoroutineScope).launch(context, start, block)
}