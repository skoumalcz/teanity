package com.skoumal.teanity.example.ui.login

import com.evernote.android.state.State
import com.skoumal.teanity.example.R
import com.skoumal.teanity.example.model.Model
import com.skoumal.teanity.example.ui.events.SnackbarEvent
import com.skoumal.teanity.example.util.isEmail
import com.skoumal.teanity.example.util.isPassword
import com.skoumal.teanity.extensions.applySchedulers
import com.skoumal.teanity.extensions.applyViewModel
import com.skoumal.teanity.util.KObservableField
import com.skoumal.teanity.viewmodel.LoadingViewModel

class LoginEmailViewModel(
    private val model: Model
) : LoadingViewModel() {

    @State
    var email = KObservableField("")
    @State
    var emailError = KObservableField("")
    @State
    var password = KObservableField("")
    @State
    var passwordError = KObservableField("")

    init {
        setLoaded()
    }

    fun loginButtonClicked() {
        val email = email.value
        val password = password.value

        val isClear = email.isEmail(emailError) or
                password.isPassword(passwordError)

        if (!isClear) return

        model.login(email)
            .applyViewModel(this)
            .applySchedulers()
            .subscribe({
                loginSucceeded()
            }, {
                loginFailed()
            })
            .add()
    }

    private fun loginSucceeded() {
        LoginEmailFragment.EVENT_NAVIGATE_TO_MAIN_ACTIVITY.publish()
    }

    private fun loginFailed() {
        SnackbarEvent(R.string.login_failed).publish()
    }
}
