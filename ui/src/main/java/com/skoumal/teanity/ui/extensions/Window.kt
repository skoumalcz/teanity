package com.skoumal.teanity.ui.extensions

import android.app.Activity
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import com.skoumal.teanity.extensions.colorAttr
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/** Applies translucency for API >= 21 && < 29 */
@OptIn(ExperimentalContracts::class)
inline fun Activity.translucency(body: EdgeToEdgeBuilderAPI21.() -> Unit) {
    contract {
        callsInPlace(body, InvocationKind.AT_MOST_ONCE)
    }
    if (
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
        Build.VERSION.SDK_INT < Build.VERSION_CODES.Q
    ) {
        EdgeToEdgeBuilderAPI21(this).apply(body).build()
        return
    }
}

/** Applies transparency for API >= 29 */
@OptIn(ExperimentalContracts::class)
inline fun Activity.transparency(
    body: EdgeToEdgeBuilder.() -> Unit
) {
    contract {
        callsInPlace(body, InvocationKind.AT_MOST_ONCE)
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        EdgeToEdgeBuilder(this).apply(body).build()
        return
    }
}

val Activity.isDarkMode
    get() = when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
        Configuration.UI_MODE_NIGHT_NO,
        Configuration.UI_MODE_NIGHT_UNDEFINED -> false
        else -> true
    }

@DslMarker
annotation class EdgeToEdgeDSL

@RequiresApi(Build.VERSION_CODES.Q)
@EdgeToEdgeDSL
open class EdgeToEdgeBuilder(
    private val activity: Activity
) {

    val isDarkMode get() = activity.isDarkMode

    private var systemUiVisibility = activity.window.decorView.systemUiVisibility

    var isNavBarLight
        get() = systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR ==
                View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        set(value) {
            systemUiVisibility = if (value) {
                systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            } else {
                systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
            }
        }

    var isStatusBarLight
        get() = systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR ==
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        set(value) {
            systemUiVisibility = if (value) {
                systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
        }

    private var navBarColor = activity.window.navigationBarColor

    var isNavBarTransparent
        get() = navBarColor == Color.TRANSPARENT
        set(value) {
            navBarColor = if (value) {
                Color.TRANSPARENT
            } else {
                activity.colorAttr(android.R.attr.navigationBarColor).defaultColor
            }
        }

    private var statusBarColor = activity.window.statusBarColor

    var isStatusBarTransparent
        get() = statusBarColor == Color.TRANSPARENT
        set(value) {
            statusBarColor = if (value) {
                Color.TRANSPARENT
            } else {
                activity.colorAttr(android.R.attr.statusBarColor).defaultColor
            }
        }

    var navBarDividerColor = activity.window.navigationBarDividerColor

    private val layoutFlags
        get() = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

    fun build() {
        with(activity.window) {
            // update ui visibility and also implicitly apply layout flags
            // why else would the user invoke edge to edge duh...
            decorView.systemUiVisibility = systemUiVisibility or layoutFlags
            // apply status bar color and remove the contrast features (rect underneath the status)
            statusBarColor = this@EdgeToEdgeBuilder.statusBarColor.also {
                if (it != Color.TRANSPARENT) return@also
                isStatusBarContrastEnforced = false
            }
            // apply nav bar color and remove the contrast features (rect underneath the nav bar)
            navigationBarColor = this@EdgeToEdgeBuilder.navBarColor.also {
                if (it != Color.TRANSPARENT) return@also
                isNavigationBarContrastEnforced = false
            }
            // also apply the divider color
            navigationBarDividerColor = this@EdgeToEdgeBuilder.navBarDividerColor
        }
    }

}

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
@EdgeToEdgeDSL
open class EdgeToEdgeBuilderAPI21(
    private val activity: Activity
) {

    private var windowFlags = activity.window.attributes.flags

    var isStatusBarTranslucent
        get() = windowFlags and WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS ==
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        set(value) {
            windowFlags = if (value) {
                windowFlags or WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            } else {
                windowFlags and WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS.inv()
            }
        }

    var isNavBarTranslucent
        get() = windowFlags and WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION ==
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
        set(value) {
            windowFlags = if (value) {
                windowFlags or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
            } else {
                windowFlags and WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION.inv()
            }
        }

    private val layoutFlags
        get() = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

    fun build() {
        with(activity.window) {
            // apply translucency flags
            attributes = attributes.also {
                it.flags = windowFlags
            }
            // apply layout flags for transparency / translucency
            decorView.systemUiVisibility = decorView.systemUiVisibility or layoutFlags
        }
    }
}
