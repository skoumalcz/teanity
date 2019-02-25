package com.skoumal.teanity.example.ui.settings

import com.skoumal.teanity.example.data.repository.RegistrationRepository
import com.skoumal.teanity.example.model.entity.Result
import com.skoumal.teanity.viewmodel.TeanityViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SettingsViewModel(
    private val registrationRepo: RegistrationRepository
) : TeanityViewModel() {

    // there should be some progress bar, but I'm too lazy
    fun logoutButtonClicked() = launch<Result<Unit>> {
        onProcess(::onProcessLogout)
        onFinished(::onFinishedLogout)
    }

    //region logout()
    private suspend fun onProcessLogout() = withContext(Dispatchers.IO) {
        registrationRepo.logout()
    }

    private fun onFinishedLogout(result: Result<Unit>) = when (result) {
        is Result.Success -> logoutSuccess()
        is Result.Error -> logoutFailed(result.exception)
    }
    //endregion

    private fun logoutSuccess() {
        SettingsFragment.EVENT_NAVIGATE_TO_LOGIN_ACTIVITY.publish()
    }

    private fun logoutFailed(throwable: Throwable) {
        // TODO
    }
}