package com.skoumal.teanity.view

import android.os.Bundle
import androidx.annotation.CallSuper
import com.evernote.android.state.StateSaver
import com.skoumal.teanity.viewevents.SimpleViewEvent
import com.skoumal.teanity.viewevents.ViewEvent

internal interface TeanityView<Binding> {

    /**
     * Called for all [ViewEvent]s published by associated viewModel.
     * For [SimpleViewEvent]s, both this and [onSimpleEventDispatched]
     * methods are called - you can choose the way how you handle them.
     */
    fun onEventDispatched(event: ViewEvent) {}

    /**
     * Called for all [SimpleViewEvent]s published by associated viewModel.
     * Both this and [onEventDispatched] methods are called - you can choose
     * the way how you handle them.
     */
    fun onSimpleEventDispatched(event: Int) {}

    fun Binding.unbindViews() {}

    /**
     * Override this method if you have more viewModels or anything else you want to restore
     * You should also override [saveState]
     */
    @CallSuper
    fun restoreState(savedInstanceState: Bundle?) {
        StateSaver.restoreInstanceState(this, savedInstanceState)
    }

    /**
     * Override this method if you have more viewModels or anything else you want to save
     * You should also override [restoreState]
     */
    @CallSuper
    fun saveState(outState: Bundle) {
        StateSaver.saveInstanceState(this, outState)
    }
}
