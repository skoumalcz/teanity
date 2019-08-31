package com.skoumal.teanity.extensions

import android.content.res.ColorStateList
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.view.ViewCompat
import com.google.android.material.snackbar.Snackbar

//region Text Color

fun Snackbar.textColorRes(@ColorRes colorRes: Int) {
    textColor(context.colorCompat(colorRes) ?: return)
}

fun Snackbar.textColor(@ColorInt color: Int) {
    textColor(ColorStateList.valueOf(color))
}

fun Snackbar.textColor(colors: ColorStateList) {
    val tv = view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
    tv.setTextColor(colors)
}

//endregion
//region Background Color

fun Snackbar.backgroundColorRes(@ColorRes colorRes: Int) {
    backgroundColor(context.colorCompat(colorRes) ?: return)
}

fun Snackbar.backgroundColor(@ColorInt color: Int) {
    backgroundColor(ColorStateList.valueOf(color))
}

fun Snackbar.backgroundColor(colors: ColorStateList) {
    ViewCompat.setBackgroundTintList(view, colors)
}

//endregion