package com.skoumal.teanity.viewevents

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.navOptions as xNavOptions

@DslMarker
annotation class NavigationDslMarker

class NavigationEvent(
    val navDirections: NavDirections,
    val navOptions: NavOptions
) : ViewEvent() {

    companion object {
        operator fun invoke(builder: NavigationEvent.Builder.() -> Unit): NavigationEvent =
            NavigationEvent.Builder().apply(builder).build()
    }

    @NavigationDslMarker
    class Builder {

        @IdRes
        var destination: Int = -1
        var navDirections: NavDirections? = null
        private var args: Bundle = Bundle()
        private var navOptions: NavOptions = xNavOptions {}

        fun args(builder: Bundle.() -> Unit) {
            args = args.apply(builder)
        }

        fun navOptions(builder: NavOptionsBuilder.() -> Unit) {
            navOptions = xNavOptions(builder)
        }

        internal fun build() = NavigationEvent(
            navDirections ?: GenericNavDirections(destination, args),
            navOptions
        )
    }
}

class GenericNavDirections(private val target: Int, private val args: Bundle) : NavDirections {

    override fun getArguments(): Bundle = args
    override fun getActionId(): Int = target

}
