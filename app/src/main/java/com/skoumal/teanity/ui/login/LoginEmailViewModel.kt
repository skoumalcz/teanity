package com.skoumal.teanity.ui.login

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.res.Resources
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import com.skoumal.teanity.R
import com.skoumal.teanity.model.Model
import com.skoumal.teanity.ui.events.SnackbarEvent
import com.skoumal.teanity.ui.events.ViewEvent
import com.skoumal.teanity.util.applySchedulers

class LoginEmailViewModel(
        private val model: Model,
        private val resources: Resources
) : ViewModel() {

    private val _viewEvents = MutableLiveData<ViewEvent>()
    val viewEvents: LiveData<ViewEvent> get() = _viewEvents

    val email = ObservableField("")
    val emailError = ObservableField("")
    val loginInProgress = ObservableBoolean(false)

    fun loginButtonClicked() {
        email.get().let { email ->
            if (email == null || !model.verifyEmail(email)) {
                emailError.set(resources.getString(R.string.login_email_error))
            } else {
                emailError.set("")
                model.login(email)
                        .applySchedulers()
                        .doOnSubscribe { loginInProgress.set(true) }
                        .subscribe({
                            loginInProgress.set(false)
                            loginSucceeded()
                        }, {
                            loginInProgress.set(false)
                            loginFailed()
                        })
            }
        }
    }

    private fun loginSucceeded() {
        _viewEvents.value = NavigateToMainActivityEvent()
    }

    private fun loginFailed() {
        _viewEvents.value = SnackbarEvent(R.string.login_failed)
    }
}