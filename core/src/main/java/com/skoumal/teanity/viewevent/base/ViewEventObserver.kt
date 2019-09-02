package com.skoumal.teanity.viewevent.base

import androidx.lifecycle.Observer

/**
 * Observer for [ViewEvent]s, which automatically checks if event was isConsumed
 */
class ViewEventObserver(private val onEventUnhandled: (ViewEvent) -> Unit) : Observer<ViewEvent> {
    override fun onChanged(event: ViewEvent?) {
        event?.let {
            if (!it.isConsumed) {
                it.consume()
                onEventUnhandled(it)
            }
        }
    }
}