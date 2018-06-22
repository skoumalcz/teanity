package com.skoumal.teanity.extensions

import android.content.Context
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat

fun Context.colorCompat(@ColorRes id: Int) = ContextCompat.getColor(this, id)
fun Context.drawableCompat(@DrawableRes id: Int) = ContextCompat.getDrawable(this, id)