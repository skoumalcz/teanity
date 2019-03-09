package com.skoumal.teanity.example.ui.login

import com.skoumal.teanity.api.Result
import com.skoumal.teanity.example.R
import com.skoumal.teanity.example.data.repository.RegistrationRepository
import com.skoumal.teanity.example.util.isEmail
import com.skoumal.teanity.example.util.isPassword
import com.skoumal.teanity.util.KObservableField
import com.skoumal.teanity.viewevents.SnackbarEvent
import com.skoumal.teanity.viewmodel.LoadingViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.evernote.android.state.State as SavedState

class LoginEmailViewModel(
    private val registrationRepo: RegistrationRepository
) : LoadingViewModel(State.LOADED) {

    @SavedState
    var email = KObservableField("")
    @SavedState
    var emailError = KObservableField("")
    @SavedState
    var password = KObservableField("")
    @SavedState
    var passwordError = KObservableField("")

    fun loginButtonClicked() = network<Unit> {
        onStart(::onStartLogin)
        onProcess(::onProcessLogin)
        onFinished(::onFinishedLogin)
    }

    //region login()
    private fun onStartLogin() {
        state = State.LOADING
    }

    private suspend fun onProcessLogin() = withContext(Dispatchers.IO) {
        registrationRepo.login {
            email = this@LoginEmailViewModel.email.value
            password = this@LoginEmailViewModel.password.value

            onEvaluate { email.isEmail(emailError) && password.isPassword(passwordError) }
        }
    }

    private fun onFinishedLogin(it: Result<Unit>) {
        state = when (it) {
            is Result.Success -> {
                loginSucceeded()
                State.LOADED
            }
            is Result.Error -> {
                loginFailed(it.exception)
                State.LOADING_FAILED
            }
        }
    }
    //endregion

    private fun loginSucceeded() {
        LoginEmailFragment.EVENT_NAVIGATE_TO_MAIN_ACTIVITY.publish()
    }

    private fun loginFailed(throwable: Throwable) {
        SnackbarEvent(R.string.login_failed).publish()
    }
}
