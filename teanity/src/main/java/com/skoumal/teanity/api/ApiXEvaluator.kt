package com.skoumal.teanity.api

open class ApiXEvaluator<SuperClass : ApiXEvaluator<SuperClass>> {

    internal var onEvaluateCallback: EvaluateCallback<SuperClass> = { true }
        private set
    internal var onEvaluateFailedCallback: () -> Unit = {}
        private set

    fun onEvaluate(callback: EvaluateCallback<SuperClass>) {
        onEvaluateCallback = callback
    }

    fun onEvaluateFailed(callback: EvaluateFailedCallback) {
        onEvaluateFailedCallback = callback
    }

    fun evaluate(): Boolean {
        val result = onEvaluateCallback(this)
        if (!result) onEvaluateFailedCallback()
        return result
    }

}

typealias EvaluateCallback<SuperClass> = ApiXEvaluator<SuperClass>.() -> Boolean
typealias EvaluateFailedCallback = () -> Unit