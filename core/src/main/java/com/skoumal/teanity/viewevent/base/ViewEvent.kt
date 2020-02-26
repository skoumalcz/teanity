package com.skoumal.teanity.viewevent.base

/**
 * Class for passing events from ViewModels to Activities/Fragments
 * Variable [isConsumed] used so each event is consumed only once
 */
abstract class ViewEvent {

    var isConsumed = false
        private set

    fun consume() {
        isConsumed = true
    }

}

/**
 * Consumes the [ViewEvent] if is instance of given [T] and executes [body].
 *
 * The [body] cannot be logically called when is the aforementioned condition `false`. It won't be
 * however called if the ViewEvent has been previously consumed. Additionally if the [body] fails
 * to complete its action normally (throws) it **WILL NOT** be marked as consumed.
 * */
inline fun <reified T> ViewEvent.consumeIfInstanceCatching(body: (T) -> Unit) = apply {
    if (!isConsumed && this is T) {
        runCatching { body(this as T) }.onFailure {
            when (this) {
                is OnFailureExecutor -> onFailure(it)
            }
        }.onSuccess { consume() }
    }
}