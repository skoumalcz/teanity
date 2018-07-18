package com.skoumal.teanity.viewmodel

import android.arch.lifecycle.ViewModel
import android.databinding.Observable
import android.databinding.PropertyChangeRegistry

/**
 * Copy of [android.databinding.BaseObservable] which extends [ViewModel]
 */
abstract class ObservableViewModel : TeanityViewModel(), Observable {

    @Transient
    private var callbacks: PropertyChangeRegistry? = null

    @Synchronized
    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback) {
        if (callbacks == null) {
            callbacks = PropertyChangeRegistry()
        }
        callbacks?.add(callback)
    }

    @Synchronized
    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback) {
        callbacks?.remove(callback)
    }

    /**
     * Notifies listeners that all properties of this instance have changed.
     */
    @Synchronized
    fun notifyChange() {
        callbacks?.notifyCallbacks(this, 0, null)
    }

    /**
     * Notifies listeners that a specific property has changed. The getter for the property
     * that changes should be marked with [android.databinding.Bindable] to generate a field in
     * `BR` to be used as `fieldId`.
     *
     * @param fieldId The generated BR id for the Bindable field.
     */
    fun notifyPropertyChanged(fieldId: Int) {
        callbacks?.notifyCallbacks(this, fieldId, null)
    }
}