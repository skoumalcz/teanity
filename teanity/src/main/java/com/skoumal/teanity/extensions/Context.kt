package com.skoumal.teanity.extensions

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

fun Context.colorCompat(@ColorRes id: Int) = ContextCompat.getColor(this, id)
fun Context.drawableCompat(@DrawableRes id: Int) = ContextCompat.getDrawable(this, id)