package com.skoumal.teanity.extensions

import android.content.res.Resources
import kotlin.math.ceil
import kotlin.math.roundToInt

fun Int.toDp(): Int = ceil((this * 1.0f).toDp()).roundToInt()
fun Int.toPx(): Int = (this * 1.0f).toPx().roundToInt()

fun Float.toDp() = this / Resources.getSystem().displayMetrics.density
fun Float.toPx() = this * Resources.getSystem().displayMetrics.density
