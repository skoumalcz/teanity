@file:Suppress("DEPRECATION")
package com.skoumal.teanity.extensions

import androidx.databinding.Observable
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.skoumal.teanity.tools.annotation.RemoveOnDeprecation
import com.skoumal.teanity.util.KObservableField

@Deprecated("Use delegated property instead")
@RemoveOnDeprecation("1.2")
fun <T> KObservableField<T>.addOnPropertyChangedCallback(
    removeAfterChanged: Boolean = false,
    callback: (T) -> Unit
) {
    addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            callback(value)
            if (removeAfterChanged) removeOnPropertyChangedCallback(this)
        }
    })
}

@Deprecated("Use delegated property instead")
@RemoveOnDeprecation("1.2")
fun ObservableInt.addOnPropertyChangedCallback(
    removeAfterChanged: Boolean = false,
    callback: (Int) -> Unit
) {
    addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            callback(get())
            if (removeAfterChanged) removeOnPropertyChangedCallback(this)
        }
    })
}

@Deprecated("Use delegated property instead")
@RemoveOnDeprecation("1.2")
fun ObservableBoolean.addOnPropertyChangedCallback(
    removeAfterChanged: Boolean = false,
    callback: (Boolean) -> Unit
) {
    addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            callback(get())
            if (removeAfterChanged) removeOnPropertyChangedCallback(this)
        }
    })
}

@Deprecated("Use delegated property instead")
@RemoveOnDeprecation("1.2")
inline fun <T> ObservableField<T>.update(block: (T?) -> T) {
    set(get().run(block))
}

@Deprecated("Use delegated property instead")
@RemoveOnDeprecation("1.2")
inline fun <T> ObservableField<T>.updateNonNull(block: (T) -> T) {
    update {
        it ?: return@updateNonNull
        block(it)
    }
}

@Deprecated("Use delegated property instead")
@RemoveOnDeprecation("1.2")
inline fun ObservableInt.update(block: (Int) -> Int) {
    set(get().run(block))
}

@Deprecated("Use delegated property instead")
@RemoveOnDeprecation("1.2")
fun KObservableField<Boolean>.toggle() {
    value = !value
}