package com.skoumal.teanity.extensions

import androidx.databinding.Observable
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.skoumal.teanity.util.KObservableField

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

inline fun <T> ObservableField<T>.update(block: (T?) -> T) {
    set(get().run(block))
}

inline fun <T> ObservableField<T>.updateNonNull(block: (T) -> T) {
    update {
        it ?: return@updateNonNull
        block(it)
    }
}

inline fun ObservableInt.update(block: (Int) -> Int) {
    set(get().run(block))
}

fun KObservableField<Boolean>.toggle() {
    value = !value
}