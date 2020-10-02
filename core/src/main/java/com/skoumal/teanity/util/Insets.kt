package com.skoumal.teanity.util

import androidx.core.view.WindowInsetsCompat
import androidx.core.graphics.Insets as AndroidInsets

@Suppress("FunctionName")
fun Insets(
    left: Int = 0,
    top: Int = 0,
    right: Int = 0,
    bottom: Int = 0
) = AndroidInsets.of(left, top, right, bottom)

internal fun WindowInsetsCompat.toPlatform() = Insets(
    systemWindowInsetLeft,
    systemWindowInsetTop,
    systemWindowInsetRight,
    systemWindowInsetBottom
)

object InsetsResources {

    @Volatile
    var isSpeculativeInsetsEnabled = true

    var insets: AndroidInsets? = null
        set(value) {
            if (isSpeculativeInsetsEnabled) {
                field = value
            }
        }

}