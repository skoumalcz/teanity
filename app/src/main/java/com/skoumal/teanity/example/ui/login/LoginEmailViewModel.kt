package com.skoumal.teanity.example.ui.login

import android.content.res.Resources
import android.databinding.ObservableBoolean
import com.evernote.android.state.State
import com.skoumal.teanity.example.R
import com.skoumal.teanity.example.model.Model
import com.skoumal.teanity.example.ui.events.SnackbarEvent
import com.skoumal.teanity.extensions.applySchedulers
import com.skoumal.teanity.util.KObservableField
import com.skoumal.teanity.viewmodel.TeanityViewModel

class LoginEmailViewModel(
    private val model: Model,
    private val resources: Resources
) : TeanityViewModel() {

    @State
    var email = KObservableField("")
    @State
    var emailError = KObservableField("")
    val loginInProgress = ObservableBoolean(false)

    fun loginButtonClicked() {
        email.value.let { email ->
            if (!model.verifyEmail(email)) {
                emailError.value = resources.getString(R.string.login_email_error)
            } else {
                emailError.value = ""
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
        LoginEmailFragment.EVENT_NAVIGATE_TO_MAIN_ACTIVITY.publish()
    }

    private fun loginFailed() {
        SnackbarEvent(R.string.login_failed).publish()
    }
}
