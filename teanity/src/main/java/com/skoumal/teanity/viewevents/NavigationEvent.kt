package com.skoumal.teanity.viewevents

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.navOptions as xNavOptions

@DslMarker
annotation class NavigationDslMarker

/**
 * Navigation event is designated as "bypass" feature for feature lacking `android.arch.navigation`. Activities extending [com.skoumal.teanity.view.TeanityActivity] (and Fragments extending [com.skoumal.teanity.view.TeanityFragment] respectively) automatically respect such events and propagate them to activity/fragment owned `navController`.
 *
 * ### There is no actual need to create actions or arguments within navigation map. Since they *will not* be used.
 * */
class NavigationEvent(
    val navDirections: NavDirections,
    val navOptions: NavOptions? = null
) : ViewEvent() {

    companion object {
        operator fun invoke(builder: NavigationEvent.Builder.() -> Unit): NavigationEvent =
            NavigationEvent.Builder().apply(builder).build()
    }

    @NavigationDslMarker
    class Builder {

        private var navOptions: NavOptions? = null
        private val directionsBuilder = NavDirectionsBuilder()

        /**
         * Passes builder to [NavDirectionsBuilder.args]
         *
         * @see [NavDirectionsBuilder.args]
         * */
        fun args(builder: Bundle.() -> Unit) = directionsBuilder.args(builder)

        /**
         * @see [androidx.navigation.navOptions]
         * */
        fun navOptions(builder: NavOptionsBuilder.() -> Unit) {
            navOptions = xNavOptions(builder)
        }

        /**
         * Updates internal [directionsBuilder]. Builder is not cleared within instance.
         * */
        fun navDirections(builder: NavDirectionsBuilder.() -> Unit) {
            directionsBuilder.apply(builder)
        }

        internal fun build() = NavigationEvent(
            directionsBuilder.build(),
            navOptions
        )
    }
}

@NavigationDslMarker
class NavDirectionsBuilder {

    /**
     * Destination will be returned by navigation library as [NavDirections.getActionId].
     *
     * Defaults to -1.
     * */
    @IdRes
    var destination: Int = -1
    private val args: Bundle = Bundle()

    /**
     * Updates internal `args` bundle. Bundle itself starts out clear however multiple calls of [args] will not reset it.
     *
     * You're strongly suggested to extension variables like so:
     *
     * ```
     * var Bundle.photoId: String?
     *      get() = getString(ID.PHOTO_ID, null)
     *      set(value) = putString(ID.PHOTO_ID, value)
     *
     * object ID {
     *      const val PHOTO_ID = "photo_id"
     * }
     * ```
     * */
    fun args(builder: Bundle.() -> Unit) = args.apply(builder)

    internal fun build() = GenericNavDirections(destination, args)

}

class GenericNavDirections(private val target: Int, private val args: Bundle) : NavDirections {

    override fun getArguments(): Bundle = args
    override fun getActionId(): Int = target

}
