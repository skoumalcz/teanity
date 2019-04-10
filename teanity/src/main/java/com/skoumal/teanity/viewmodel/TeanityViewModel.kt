package com.skoumal.teanity.viewmodel

import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.evernote.android.state.StateSaver
import com.skoumal.teanity.viewevents.SimpleViewEvent
import com.skoumal.teanity.viewevents.ViewEvent
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject

abstract class TeanityViewModel : ViewModel() {

    private val disposables = CompositeDisposable()
    private val _viewEvents = PublishSubject.create<ViewEvent>()
    val viewEvents: Observable<ViewEvent> get() = _viewEvents

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
}