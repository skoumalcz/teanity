package com.skoumal.teanity.viewevent.base

import com.skoumal.teanity.tools.log.error

interface OnFailureExecutor {

    /**
     * # Definition
     * [onFailure] is called whenever parameter of parent executor cannot be resolved or exception
     * was raised during execution of parent executor. This is important so using events on
     * unsupported platform doesn't induce crashes.
     *
     * ## Notes
     * The default implementation reports crashes through [error] messages.*/
    fun onFailure(throwable: Throwable) = error(throwable)

}