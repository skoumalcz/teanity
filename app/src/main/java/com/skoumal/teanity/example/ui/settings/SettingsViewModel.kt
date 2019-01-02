package com.skoumal.teanity.example.ui.settings

import com.skoumal.teanity.example.model.entity.Result
import com.skoumal.teanity.example.data.repository.RegistrationRepository
import com.skoumal.teanity.viewmodel.TeanityViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SettingsViewModel(
    private val registrationRepo: RegistrationRepository
) : TeanityViewModel() {

    fun logoutButtonClicked() {
        // there should be some progress bar, but I'm too lazy
        launch {
            val result = withContext(Dispatchers.IO) {
                registrationRepo.logout()
            }

            when (result) {
                is Result.Success -> logoutSuccess()
                is Result.Error -> logoutFailed(result.exception)
            }
        }
    }

    private fun logoutSuccess() {
        SettingsFragment.EVENT_NAVIGATE_TO_LOGIN_ACTIVITY.publish()
    }

    private fun logoutFailed(throwable: Throwable) {
        // TODO
    }
}