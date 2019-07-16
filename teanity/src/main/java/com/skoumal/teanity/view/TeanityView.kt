package com.skoumal.teanity.view

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.FragmentNavigator
import com.evernote.android.state.StateSaver
import com.skoumal.teanity.util.Insets
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
     * Called whenever root view needs its insets readjusted. This API allows for consuming only parts of given insets
     * and gives off full control over to your use-case.
     *
     * You might want to consume bottom insets only for this view and leave the rest for the children.
     * ```
     * override fun consumeSystemWindowInsets(..., bottom: Int) = Insets(bottom = bottom)
     * ```
     *
     * You may as well consume the insets only partially, **however beware** of overflowing initial value, always
     * min/max these values.
     * */
    fun consumeSystemWindowInsets(left: Int, top: Int, right: Int, bottom: Int): Insets

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

internal fun NavController.navigate(
    navDirections: NavDirections,
    navOptions: NavOptions?,
    extras: FragmentNavigator.Extras?
) = navigate(navDirections.actionId, navDirections.arguments, navOptions, extras)