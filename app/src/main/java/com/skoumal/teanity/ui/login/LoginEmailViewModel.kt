package com.skoumal.teanity.ui.login

import android.content.res.Resources
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import com.skoumal.teanity.R
import com.skoumal.teanity.model.Model
import com.skoumal.teanity.ui.base.BaseViewModel
import com.skoumal.teanity.ui.events.SnackbarEvent
import com.skoumal.teanity.util.applySchedulers

class LoginEmailViewModel(
        private val model: Model,
        private val resources: Resources
) : BaseViewModel() {

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
        NavigateToMainActivityEvent().publish()
    }

    private fun loginFailed() {
        SnackbarEvent(R.string.login_failed).publish()
    }
}