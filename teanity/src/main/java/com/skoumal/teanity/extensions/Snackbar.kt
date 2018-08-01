package com.skoumal.teanity.extensions

import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import com.google.android.material.R

fun AppCompatActivity.snackbar(
    view: View,
    @StringRes messageRes: Int,
    length: Int = Snackbar.LENGTH_SHORT,
    f: Snackbar.() -> Unit = {}
) {
    snackbar(view, getString(messageRes), length, f)
}

fun AppCompatActivity.snackbar(
    view: View,
    message: String,
    length: Int = Snackbar.LENGTH_SHORT,
    f: Snackbar.() -> Unit = {}
) {
    val snack = Snackbar.make(view, message, length)
    snack.f()
    snack.show()
}

fun Fragment.snackbar(
    view: View,
    @StringRes messageRes: Int,
    length: Int = Snackbar.LENGTH_SHORT,
    f: Snackbar.() -> Unit = {}
) {
    snackbar(view, getString(messageRes), length, f)
}

fun Fragment.snackbar(
    view: View,
    message: String,
    length: Int = Snackbar.LENGTH_SHORT,
    f: Snackbar.() -> Unit = {}
) {
    val snack = Snackbar.make(view, message, length)
    snack.f()
    snack.show()
}

fun Snackbar.action(
    @StringRes actionRes: Int,
    @ColorRes colorRes: Int? = null,
    listener: (View) -> Unit
) {
    action(
        view.resources.getString(actionRes),
        colorRes?.let { ContextCompat.getColor(view.context, colorRes) },
        listener
    )
}

fun Snackbar.action(action: String, @ColorInt color: Int? = null, listener: (View) -> Unit) {
    setAction(action, listener)
    color?.let { setActionTextColor(color) }
}

fun Snackbar.textColorRes(@ColorRes colorRes: Int) {
    textColor(context.colorCompat(colorRes))
}

fun Snackbar.textColor(@ColorInt color: Int) {
    val tv = view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
    tv.setTextColor(color)
}

fun Snackbar.alert() {
    textColor(0xF44336)
}

fun Snackbar.success() {
    textColor(0x4CAF50)
}