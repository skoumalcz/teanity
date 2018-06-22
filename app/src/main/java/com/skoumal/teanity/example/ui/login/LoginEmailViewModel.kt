package com.skoumal.teanity.example.ui.login

import android.content.res.Resources
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import com.evernote.android.state.State
import com.skoumal.teanity.example.R
import com.skoumal.teanity.example.model.Model
import com.skoumal.teanity.example.ui.events.SnackbarEvent
import com.skoumal.teanity.extensions.applySchedulers
import com.skoumal.teanity.viewmodel.TeanityViewModel

class LoginEmailViewModel(
    private val model: Model,
    private val resources: Resources
) : TeanityViewModel() {

    @State
    var email = ObservableField("")
    @State
    var emailError = ObservableField("")
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
                    .add()
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