package com.skoumal.teanity.view

import androidx.core.graphics.Insets
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.FragmentNavigator
import com.skoumal.teanity.viewevent.base.ViewEvent

internal interface TeanityView<Binding> {

    /**
     * Called for all [ViewEvent]s published by associated viewModel.
     */
    fun onEventDispatched(event: ViewEvent) {}

    fun Binding.unbindViews() {}

    /**
     * Called whenever root view needs its insets readjusted. This API allows for consuming only parts of given insets
     * and gives off full control over to your use-case.
     *
     * You might want to consume bottom insets only for this view and leave the rest for the children.
     * ```
     * override fun consumeSystemWindowInsets(insets: Insets) = Insets(bottom = bottom)
     * ```
     *
     * You may as well consume the insets only partially, **however beware** of overflowing initial value, always
     * min/max these values.
     * */
    fun consumeSystemWindowInsets(insets: Insets): Insets? = null

    /**
     * Called before [consumeSystemWindowInsets]. This will not consume insets which is beneficial
     * when wanting to pass insets to child fragments as well as keep them in your activity for
     * various reasons.
     *
     *
     * Keep in mind that by not consuming your insets it **will** be passed to your offsprings which
     * might cause inconsistencies down the line. **Use with caution.**
     * */
    fun peekSystemWindowInsets(insets: Insets) {}

}

internal fun NavController.navigate(
    navDirections: NavDirections,
    navOptions: NavOptions?,
    extras: FragmentNavigator.Extras?
) = navigate(navDirections.actionId, navDirections.arguments, navOptions, extras)