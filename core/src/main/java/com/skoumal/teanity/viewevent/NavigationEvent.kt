package com.skoumal.teanity.viewevent

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.skoumal.teanity.view.TeanityActivity
import com.skoumal.teanity.viewevent.base.ActivityExecutor
import com.skoumal.teanity.viewevent.base.ViewEvent
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
    val navOptions: NavOptions? = null,
    private val extras: FragmentNavigator.Extras? = null,
    private val pendingExtras: Array<out Pair<Int, String>>? = null
) : ViewEvent(), ActivityExecutor {

    override fun invoke(activity: AppCompatActivity) {
        if (activity !is TeanityActivity<*, *>) return
        activity.apply { navigate() }
    }

    fun getExtras(activity: Activity) = activity.run {
        extras ?: pendingExtras
            ?.map { findViewById<View>(it.first) to it.second }
            ?.let { FragmentNavigatorExtras(*it.toTypedArray()) }
    }

    fun getExtras(fragment: Fragment) = fragment.run {
        extras ?: view?.let { view ->
            pendingExtras
                ?.map { view.findViewById<View>(it.first) to it.second }
                ?.let { FragmentNavigatorExtras(*it.toTypedArray()) }
        }
    }

    companion object {
        operator fun invoke(builder: Builder.() -> Unit): NavigationEvent =
            Builder().apply(builder).build()
    }

    @NavigationDslMarker
    class Builder {

        private var navOptions: NavOptions? = null
        private var extras: FragmentNavigator.Extras? = null
        private var pendingExtras: Array<out Pair<Int, String>>? = null
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

        /**
         * Builds extras for view transitions
         * */
        fun extras(vararg transitions: Pair<View, String>) {
            extras = FragmentNavigatorExtras(*transitions)
        }

        fun extrasIds(vararg transitions: Pair<Int, String>) {
            pendingExtras = transitions
        }

        internal fun build() = NavigationEvent(
            directionsBuilder.build(),
            navOptions,
            extras,
            pendingExtras
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

    /**
     * Current task (activity) will be finished after navigated to another destination. Make sure this this used only
     * in conjunction with destination being activity as it will **always** finish it.
     *
     * Defaults to false.
     * */
    var clearTask: Boolean = false
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

    internal fun build() = GenericNavDirections(destination, args).apply {
        clearTask = this@NavDirectionsBuilder.clearTask
    }

}

class GenericNavDirections(private val target: Int, private val args: Bundle) : NavDirections {

    var clearTask: Boolean = false

    override fun getArguments(): Bundle = args
    override fun getActionId(): Int = target

}
