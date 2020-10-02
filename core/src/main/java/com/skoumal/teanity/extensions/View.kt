package com.skoumal.teanity.extensions

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import com.skoumal.teanity.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Deprecated("Toggling keyboards can result in unwanted behavior, use showKeyboard or hideKeyboard")
fun Activity.toggleKeyboard(focusView: View? = null) {
    focusView?.requestFocus()
    inputMethodManager.toggleSoftInput(
        InputMethodManager.SHOW_IMPLICIT,
        InputMethodManager.HIDE_IMPLICIT_ONLY
    )
}

@Deprecated("Toggling keyboards can result in unwanted behavior, use showKeyboard or hideKeyboard")
fun Fragment.toggleKeyboard(focusView: View? = null) {
    @Suppress("DEPRECATION")
    activity?.toggleKeyboard(focusView)
}


fun AppCompatActivity.showKeyboard(view: View) {
    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.R) showKeyboardAPI30(view)
    else showKeyboardLegacy(view)
}

fun Fragment.showKeyboard(view: View) {
    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.R) showKeyboardAPI30(view)
    else showKeyboardLegacy(view)
}

fun AppCompatActivity.hideKeyboard(view: View? = null) {
    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.R) hideKeyboardAPI30(view)
    else hideKeyboardLegacy(view)
}

fun Fragment.hideKeyboard(view: View? = null) {
    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.R) hideKeyboardAPI30(view)
    else hideKeyboardLegacy(view)
}

// region Legacy

internal fun AppCompatActivity.showKeyboardLegacy(view: View) {
    (this as Context).showKeyboardLegacy(view)
}

internal fun Fragment.showKeyboardLegacy(view: View) {
    requireContext().showKeyboardLegacy(view)
}

internal fun Fragment.hideKeyboardLegacy(focusedView: View? = null) {
    val view = focusedView ?: view ?: activity?.currentFocus ?: activity?.window?.decorView
    requireContext().inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
}

internal fun AppCompatActivity.hideKeyboardLegacy(focusedView: View? = null) {
    val view = focusedView ?: currentFocus ?: window.decorView
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

internal fun Context.showKeyboardLegacy(view: View) {
    view.requestFocus()
    inputMethodManager.showSoftInput(
        view,
        InputMethodManager.SHOW_IMPLICIT
    )
}

// endregion

// region Android 11 (30)

@RequiresApi(Build.VERSION_CODES.R)
internal fun AppCompatActivity.showKeyboardAPI30(view: View) {
    lifecycleScope.show(WindowInsets.Type.ime(), view)
}

@RequiresApi(Build.VERSION_CODES.R)
internal fun Fragment.showKeyboardAPI30(view: View) {
    lifecycleScope.show(WindowInsets.Type.ime(), view)
}

@RequiresApi(Build.VERSION_CODES.R)
internal fun Fragment.hideKeyboardAPI30(focusedView: View? = null) {
    val view = focusedView ?: view ?: activity?.currentFocus
    lifecycleScope.hide(WindowInsets.Type.ime(), view ?: return hideKeyboardLegacy())
}

@RequiresApi(Build.VERSION_CODES.R)
internal fun AppCompatActivity.hideKeyboardAPI30(focusedView: View? = null) {
    val view = focusedView ?: currentFocus
    lifecycleScope.hide(WindowInsets.Type.ime(), view ?: return hideKeyboardLegacy())
}

@RequiresApi(Build.VERSION_CODES.R)
internal fun LifecycleCoroutineScope.hide(type: Int, view: View) = launchWhenCreated {
    view.awaitWindowInsetsController().hide(type)
}

@RequiresApi(Build.VERSION_CODES.R)
internal fun LifecycleCoroutineScope.show(type: Int, view: View): Job? {
    return launchWhenCreated {
        view.awaitWindowInsetsController().show(type)
    }
}

// endregion

// region Helpers

var View.scope: CoroutineScope
    get() = getTag(R.id.coroutineScope) as? CoroutineScope ?: GlobalScope.also { scope = it }
    set(value) = setTag(R.id.coroutineScope, value)

private val Context.inputMethodManager
    get() = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

@RequiresApi(Build.VERSION_CODES.R)
suspend fun View.awaitWindowInsetsController(): WindowInsetsController {
    return windowInsetsController
        ?: suspendCoroutine {
            setOnGlobalLayoutListener { view ->
                it.resume(view.windowInsetsController)
            }
        }
        ?: awaitWindowInsetsController()
}

inline fun <V : View> V.setOnGlobalLayoutListener(crossinline body: (V) -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            viewTreeObserver.removeOnGlobalLayoutListener(this)
            body(this@setOnGlobalLayoutListener)
        }
    })
}

// endregion