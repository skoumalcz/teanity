package com.skoumal.teanity.ui.settings

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.skoumal.teanity.model.Model
import com.skoumal.teanity.ui.events.ViewEvent
import com.skoumal.teanity.util.applySchedulers

class SettingsViewModel(
        private val model: Model
) : ViewModel() {

    private val _viewEvents = MutableLiveData<ViewEvent>()
    val viewEvents: LiveData<ViewEvent> get() = _viewEvents

    fun logoutButtonClicked() {
        // there should be some progress bar, but I'm too lazy
        model.logout()
                .applySchedulers()
                .subscribe({
                    logoutSuccess()
                }, {
                    logoutFailed(it)
                })
    }

    private fun logoutSuccess() {
        _viewEvents.value = NavigateToLoginActivityEvent()
    }

    private fun logoutFailed(throwable: Throwable) {
        // TODO
    }
}