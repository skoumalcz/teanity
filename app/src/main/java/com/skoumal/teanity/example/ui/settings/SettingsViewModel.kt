package com.skoumal.teanity.example.ui.settings

import com.skoumal.teanity.example.data.repository.RegistrationRepository
import com.skoumal.teanity.extensions.applySchedulers
import com.skoumal.teanity.extensions.subscribeK
import com.skoumal.teanity.viewmodel.TeanityViewModel

class SettingsViewModel(
    private val registrationRepo: RegistrationRepository
) : TeanityViewModel() {

    fun logoutButtonClicked() {
        // there should be some progress bar, but I'm too lazy
        registrationRepo.logout()
            .applySchedulers()
            .subscribeK(onComplete = this::logoutSuccess, onError = this::logoutFailed)
            .add()
    }

    private fun logoutSuccess() {
        SettingsFragment.EVENT_NAVIGATE_TO_LOGIN_ACTIVITY.publish()
    }

    private fun logoutFailed(throwable: Throwable) {
        // TODO
    }
}