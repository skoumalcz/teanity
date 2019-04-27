package com.skoumal.teanity.viewmodel

import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.evernote.android.state.StateSaver
import com.skoumal.teanity.api.Result
import com.skoumal.teanity.coroutine.CoroutineChain
import com.skoumal.teanity.viewevents.SimpleViewEvent
import com.skoumal.teanity.viewevents.ViewEvent
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import io.reactivex.subjects.PublishSubject

abstract class TeanityViewModel : ViewModel(), CoroutineScope {

    private val disposables = CompositeDisposable()

    private val parentJob = SupervisorJob()
    override val coroutineContext: CoroutineContext = Dispatchers.Main + parentJob

    private val _viewEvents = PublishSubject.create<ViewEvent>()
    val viewEvents: Observable<ViewEvent> get() = _viewEvents

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
        parentJob.cancel()
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

    protected fun <Target> async(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: CoroutineChain.Builder<Target>.() -> Unit
    ): Job {
        val task = CoroutineChain.Builder<Target>().apply(block).build()
        return launch(context, start) {
            task.chain()
        }
    }

    protected fun <Target : Any> network(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: CoroutineChain.Builder<Result<Target>>.() -> Unit
    ): Job {
        val task = CoroutineChain.Builder<Result<Target>>().apply(block).build()
        return launch(context, start) {
            task.chain()
        }
    }
}