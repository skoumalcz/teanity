package com.skoumal.teanity.util

import androidx.databinding.Observable
import androidx.databinding.ObservableField
import java.io.Serializable

/**
 * Kotlin version of [ObservableField].
 * You can define if wrapped type is Nullable or not.
 * You can use kotlin get/set syntax for value
 */
class KObservableField<T> : ObservableField<T>, Serializable {

    @Volatile
    var value: T
        @Synchronized set(value) {
            if (field != value) {
                field = value
                notifyChange()
            }
        }

    constructor(init: T) {
        value = init
    }

    constructor(init: T, vararg dependencies: Observable) : super(*dependencies) {
        value = init
    }

    @Deprecated(
        message = "Needed for data binding, use KObservableField.value syntax from code",
        replaceWith = ReplaceWith("value"),
        level = DeprecationLevel.HIDDEN
    )
    override fun get(): T {
        return value
    }

    @Deprecated(
        message = "Needed for data binding, use KObservableField.value = ... syntax from code",
        replaceWith = ReplaceWith("value = newValue"),
        level = DeprecationLevel.HIDDEN
    )
    override fun set(newValue: T) {
        value = newValue
    }

    override fun toString(): String {
        return "KObservableField(value=$value)"
    }
}