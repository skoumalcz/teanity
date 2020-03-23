package com.skoumal.teanity.extensions

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat

fun Context.colorCompat(@ColorRes id: Int) = try {
    ContextCompat.getColor(this, id)
} catch (e: Resources.NotFoundException) {
    null
}

fun Context.colorStateListCompat(@ColorRes id: Int) = try {
    AppCompatResources.getColorStateList(this, id)
} catch (e: Resources.NotFoundException) {
    null
}

fun Context.drawableCompat(@DrawableRes id: Int) = AppCompatResources.getDrawable(this, id)

/**
 * Pass [start] and [end] dimensions, function will return left and right
 * with respect to RTL layout direction
 */
fun Context.startEndToLeftRight(start: Int, end: Int): Pair<Int, Int> {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 &&
        resources.configuration.layoutDirection == View.LAYOUT_DIRECTION_RTL
    ) {
        return end to start
    }
    return start to end
}