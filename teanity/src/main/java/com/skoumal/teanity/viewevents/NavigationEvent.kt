package com.skoumal.teanity.viewevents

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.navOptions as xNavOptions

@DslMarker
annotation class NavigationDslMarker

class NavigationEvent(
    val destination: Int,
    val args: Bundle,
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
        private var args: Bundle = Bundle()
        private var navOptions: NavOptions = xNavOptions {}

        fun args(builder: Bundle.() -> Unit) {
            args = args.apply(builder)
        }

        fun navOptions(builder: NavOptionsBuilder.() -> Unit) {
            navOptions = xNavOptions(builder)
        }

        internal fun build() = NavigationEvent(destination, args, navOptions)
    }

}