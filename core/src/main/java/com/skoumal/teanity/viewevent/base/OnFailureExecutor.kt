package com.skoumal.teanity.viewevent.base

import timber.log.Timber

interface OnFailureExecutor {

    /**
     * # Definition
     * [onFailure] is called whenever parameter of parent executor cannot be resolved or exception
     * was raised during execution of parent executor. This is important so using events on
     * unsupported platform doesn't induce crashes.
     *
     * ## Notes
     * The default implementation reports crashes through [Timber] error messages.*/
    fun onFailure(throwable: Throwable) = Timber.e(throwable)

}