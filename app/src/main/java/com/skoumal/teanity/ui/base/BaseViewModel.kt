package com.skoumal.teanity.ui.base

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.skoumal.teanity.ui.events.ViewEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel : ViewModel() {

    private val disposables = CompositeDisposable()
    private val _viewEvents = MutableLiveData<ViewEvent>()
    val viewEvents: LiveData<ViewEvent> get() = _viewEvents

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    fun <Event : ViewEvent> Event.publish() {
        _viewEvents.value = this
    }

    fun Disposable.add() {
        disposables.add(this)
    }
}