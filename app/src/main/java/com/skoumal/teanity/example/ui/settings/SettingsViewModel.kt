package com.skoumal.teanity.example.ui.settings

import com.skoumal.teanity.example.model.Model
import com.skoumal.teanity.extensions.applySchedulers
import com.skoumal.teanity.viewmodel.TeanityViewModel

class SettingsViewModel(
    private val model: Model
) : TeanityViewModel() {

    fun logoutButtonClicked() {
        // there should be some progress bar, but I'm too lazy
        model.logout()
            .applySchedulers()
            .subscribe({
                logoutSuccess()
            }, {
                logoutFailed(it)
            })
            .add()
    }

    private fun logoutSuccess() {
        NavigateToLoginActivityEvent().publish()
    }

    private fun logoutFailed(throwable: Throwable) {
        // TODO
    }
}