package com.skoumal.teanity.extensions

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import android.view.View
import android.view.inputmethod.InputMethodManager

fun Activity.showKeyboard(view: View) {
    view.requestFocus()
    inputMethodManager.showSoftInput(
        view,
        InputMethodManager.SHOW_IMPLICIT
    )
}

fun Fragment.showKeyboard(view: View) {
    activity?.showKeyboard(view)
}

fun Activity.toggleKeyboard(focusView: View? = null) {
    focusView?.requestFocus()
    inputMethodManager.toggleSoftInput(
        InputMethodManager.SHOW_IMPLICIT,
        InputMethodManager.HIDE_IMPLICIT_ONLY
    )
}

fun Fragment.toggleKeyboard(focusView: View? = null) {
    activity?.toggleKeyboard(focusView)
}

fun Activity.hideKeyboard() {
    currentFocus?.let {
        inputMethodManager.hideSoftInputFromWindow(
            it.windowToken,
            0
        )
    }
}

fun Fragment.hideKeyboard() {
    activity?.hideKeyboard()
}

fun FragmentManager.fragmentTransaction(actions: FragmentTransaction.() -> Unit) {
    beginTransaction().apply(actions).commit()
}

private val Context.inputMethodManager get() =
    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
