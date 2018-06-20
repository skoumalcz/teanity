package com.skoumal.teanity.ui.base

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.skoumal.teanity.ui.events.ViewEvent

abstract class BaseViewModel : ViewModel() {

    private val _viewEvents = MutableLiveData<ViewEvent>()
    val viewEvents: LiveData<ViewEvent> get() = _viewEvents

    fun <Event : ViewEvent> Event.publish() {
        _viewEvents.value = this
    }

}