package com.skoumal.teanity.ui.settings

import com.skoumal.teanity.model.Model
import com.skoumal.teanity.ui.base.BaseViewModel
import com.skoumal.teanity.util.applySchedulers

class SettingsViewModel(
    private val model: Model
) : BaseViewModel() {

    fun logoutButtonClicked() {
        // there should be some progress bar, but I'm too lazy
        model.logout()
            .applySchedulers()
            .subscribe({
                logoutSuccess()
            }, {
                logoutFailed(it)
            })
    }

    private fun logoutSuccess() {
        NavigateToLoginActivityEvent().publish()
    }

    private fun logoutFailed(throwable: Throwable) {
        // TODO
    }
}