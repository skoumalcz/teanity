package com.skoumal.teanity.ui.events

import android.arch.lifecycle.Observer

/**
 * Observer for [ViewEvent]s, which automatically checks if event was handled
 */
class ViewEventObserver(private val onEventUnhandled: (ViewEvent) -> Unit) : Observer<ViewEvent> {
    override fun onChanged(event: ViewEvent?) {
        event?.let {
            if (!it.handled) {
                it.handled = true
                onEventUnhandled(it)
            }
        }
    }
}