package com.skoumal.teanity.extensions

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import android.view.View
import android.view.inputmethod.InputMethodManager

fun Activity.showKeyboard(focusView: View? = null) {
    focusView?.requestFocus()
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
}

fun Fragment.showKeyboard(focusView: View? = null) {
    activity?.showKeyboard(focusView)
}

fun Activity.hideKeyboard() {
    currentFocus?.let {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
    }
}

fun Fragment.hideKeyboard() {
    activity?.hideKeyboard()
}

fun FragmentManager.fragmentTransaction(actions: FragmentTransaction.() -> Unit) {
    beginTransaction().apply(actions).commit()
}