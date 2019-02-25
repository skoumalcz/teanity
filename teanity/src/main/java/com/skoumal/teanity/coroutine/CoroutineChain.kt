package com.skoumal.teanity.coroutine

class CoroutineChain<Target> private constructor(
    private val onStartCallback: OnStartCallback,
    private val onProcessCallback: OnProcessCallback<Target>,
    private val onFinishedCallback: OnFinishedCallback<Target>
) {

    private fun onStart() = onStartCallback()
    private suspend fun onProcess() = onProcessCallback()
    private fun onFinished(target: Target) = onFinishedCallback(target)

    suspend fun chain() {
        onStart()
        onFinished(onProcess())
    }

    class Builder<Target> {

        private var onStartCallback: OnStartCallback = {}
        private var onProcessCallback: OnProcessCallback<Target> =
            { throw IllegalStateException("Target must be returned") }
        private var onFinishedCallback: OnFinishedCallback<Target> = {}

        fun onStart(callback: OnStartCallback) {
            onStartCallback = callback
        }

        fun onProcess(callback: OnProcessCallback<Target>) {
            onProcessCallback = callback
        }

        fun onFinished(callback: OnFinishedCallback<Target>) {
            onFinishedCallback = callback
        }

        internal fun build() = CoroutineChain(onStartCallback, onProcessCallback, onFinishedCallback)
    }

}

typealias OnStartCallback = () -> Unit
typealias OnProcessCallback<Target> = suspend () -> Target
typealias OnFinishedCallback<Target> = (Target) -> Unit