package com.skoumal.teanity.viewmodel

import android.os.Bundle
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

abstract class TeanityViewModel : ViewModel(), CoroutineScope {

    private val disposables = CompositeDisposable()

    override val coroutineContext: CoroutineContext get() = viewModelScope.coroutineContext

    private val _viewEvents = PublishSubject.create<ViewEvent>()
    val viewEvents: Observable<ViewEvent> get() = _viewEvents

    val insets = KObservableField(Insets.empty)

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
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