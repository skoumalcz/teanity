package com.skoumal.teanity.example.ui.settings

import com.skoumal.teanity.api.Result
import com.skoumal.teanity.example.data.repository.RegistrationRepository
import com.skoumal.teanity.viewmodel.TeanityViewModel

class SettingsViewModel(
    private val registrationRepo: RegistrationRepository
) : TeanityViewModel() {

    // there should be some progress bar, but I'm too lazy
    fun logoutButtonClicked() = launch {
        val result = registrationRepo.logout()
        onFinishedLogout(result)
    }

    //region logout()
    private fun onFinishedLogout(result: Result<Unit>) = when (result) {
        is Result.Success -> logoutSuccess()
        is Result.Error -> logoutFailed(result.exception)
        is Result.Void -> logoutSuccess()
    }
    //endregion

    private fun logoutSuccess() {
        SettingsFragment.EVENT_NAVIGATE_TO_LOGIN_ACTIVITY.publish()
    }

    private fun logoutFailed(throwable: Throwable) {
        // TODO
    }
}