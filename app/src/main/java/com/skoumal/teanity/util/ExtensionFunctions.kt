package com.skoumal.teanity.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.databinding.*
import android.provider.Settings
import android.support.annotation.*
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.net.toUri
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import io.reactivex.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import android.databinding.Observable as BindingObservable

/* ==== RxJava ===================================================================================*/

fun <T> Observable<T>.applySchedulers(
    subscribeOn: Scheduler = Schedulers.io(),
    observeOn: Scheduler = AndroidSchedulers.mainThread()
): Observable<T> = this.subscribeOn(subscribeOn).observeOn(observeOn)

fun <T> Flowable<T>.applySchedulers(
    subscribeOn: Scheduler = Schedulers.io(),
    observeOn: Scheduler = AndroidSchedulers.mainThread()
): Flowable<T> = this.subscribeOn(subscribeOn).observeOn(observeOn)

fun <T> Single<T>.applySchedulers(
    subscribeOn: Scheduler = Schedulers.io(),
    observeOn: Scheduler = AndroidSchedulers.mainThread()
): Single<T> = this.subscribeOn(subscribeOn).observeOn(observeOn)

fun <T> Maybe<T>.applySchedulers(
    subscribeOn: Scheduler = Schedulers.io(),
    observeOn: Scheduler = AndroidSchedulers.mainThread()
): Maybe<T> = this.subscribeOn(subscribeOn).observeOn(observeOn)

fun Completable.applySchedulers(
    subscribeOn: Scheduler = Schedulers.io(),
    observeOn: Scheduler = AndroidSchedulers.mainThread()
): Completable = this.subscribeOn(subscribeOn).observeOn(observeOn)

/* ==== SnackBar =================================================================================*/

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
    val tv = view.findViewById<TextView>(android.support.design.R.id.snackbar_text)
    tv.setTextColor(color)
}

fun Snackbar.alert() {
    textColor(0xF44336)
}

fun Snackbar.success() {
    textColor(0x4CAF50)
}

/* ==== Activities and Fragments =================================================================*/

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
        inputMethodManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
    }
}

fun Fragment.hideKeyboard() {
    activity?.hideKeyboard()
}

fun FragmentManager.fragmentTransaction(actions: FragmentTransaction.() -> Unit) {
    beginTransaction().apply(actions).commit()
}

/* ==== Data binding =============================================================================*/

fun <T : ViewDataBinding> AppCompatActivity.setBindingContentView(@LayoutRes layoutResId: Int): T {
    return DataBindingUtil.setContentView<T>(this, layoutResId).apply {
        setLifecycleOwner(this@setBindingContentView)
    }
}

fun <T : ViewDataBinding> Fragment.inflateBindingView(
    inflater: LayoutInflater,
    @LayoutRes layoutResId: Int,
    parent: ViewGroup?,
    attachToParent: Boolean
): T {
    return DataBindingUtil.inflate<T>(inflater, layoutResId, parent, attachToParent).apply {
        setLifecycleOwner(this@inflateBindingView)
    }
}

fun <T> ObservableField<T>.addOnPropertyChangedCallback(
    removeAfterChanged: Boolean = false,
    callback: (T?) -> Unit
) {
    addOnPropertyChangedCallback(object :
        android.databinding.Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: android.databinding.Observable?, propertyId: Int) {
            callback(get())
            if (removeAfterChanged) removeOnPropertyChangedCallback(this)
        }
    })
}

fun ObservableInt.addOnPropertyChangedCallback(
    removeAfterChanged: Boolean = false,
    callback: (Int) -> Unit
) {
    addOnPropertyChangedCallback(object :
        BindingObservable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: BindingObservable?, propertyId: Int) {
            callback(get())
            if (removeAfterChanged) removeOnPropertyChangedCallback(this)
        }
    })
}

fun ObservableBoolean.addOnPropertyChangedCallback(
    removeAfterChanged: Boolean = false,
    callback: (Boolean) -> Unit
) {
    addOnPropertyChangedCallback(object :
        BindingObservable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: BindingObservable?, propertyId: Int) {
            callback(get())
            if (removeAfterChanged) removeOnPropertyChangedCallback(this)
        }
    })
}

inline fun <T> ObservableField<T>.update(block: (T?) -> Unit) {
    set(get().apply(block))
}

inline fun <T> ObservableField<T>.updateNonNull(block: (T) -> Unit) {
    update {
        it ?: return@update
        block(it)
    }
}

inline fun ObservableInt.update(block: (Int) -> Unit) {
    set(get().apply(block))
}

/* ==== Permissions ==============================================================================*/

fun AppCompatActivity.requestPermission(
    permission: String,
    explanation: String,
    view: View,
    onGranted: () -> Unit,
    onDenied: () -> Unit
) {
    Dexter.withActivity(this)
        .withPermission(permission)
        .withListener(object : PermissionListener {
            override fun onPermissionRationaleShouldBeShown(
                permission: PermissionRequest,
                token: PermissionToken
            ) = token.continuePermissionRequest()

            override fun onPermissionDenied(response: PermissionDeniedResponse) {
                onDenied()
                if (response.isPermanentlyDenied) {
                    snackbar(view, explanation, Snackbar.LENGTH_LONG) {
                        //TODO: replace with your string resource
                        action("Settings") {
                            val appSettings = Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                "package:$packageName".toUri()
                            ).apply {
                                addCategory(Intent.CATEGORY_DEFAULT)
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            }

                            startActivity(appSettings)
                        }
                    }
                }
            }

            override fun onPermissionGranted(response: PermissionGrantedResponse?) = onGranted()

        })
        .check()
}

fun AppCompatActivity.requestPermissionSoft(permission: String, onGranted: () -> Unit, onDenied: () -> Unit) {
    Dexter.withActivity(this)
        .withPermission(permission)
        .withListener(object : PermissionListener {
            override fun onPermissionGranted(response: PermissionGrantedResponse) {
                onGranted()
            }

            override fun onPermissionDenied(response: PermissionDeniedResponse) {
                onDenied()
            }

            override fun onPermissionRationaleShouldBeShown(
                permission: PermissionRequest,
                token: PermissionToken
            ) {
                token.cancelPermissionRequest()
            }
        })
        .check()
}

/* ==== Misc =====================================================================================*/

fun Context.colorCompat(@ColorRes id: Int) = ContextCompat.getColor(this, id)
fun Resources.colorCompat(@ColorRes id: Int) = ResourcesCompat.getColor(this, id, null)
fun Context.drawableCompat(@DrawableRes id: Int) = ContextCompat.getDrawable(this, id)