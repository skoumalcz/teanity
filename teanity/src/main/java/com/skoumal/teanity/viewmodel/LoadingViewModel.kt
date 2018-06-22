package com.skoumal.teanity.viewmodel

import android.databinding.Bindable
import android.databinding.ObservableBoolean
import com.skoumal.teanity.BR

abstract class LoadingViewModel : ObservableViewModel() {

    var state = STATE_LOADING
        set(value) {
            field = value
            notifyPropertyChanged(BR.loading)
            notifyPropertyChanged(BR.loaded)
            notifyPropertyChanged(BR.loadingFailed)
        }

    val loading @Bindable get() = state == STATE_LOADING
    val loaded @Bindable get() = state == STATE_LOADED
    val loadingFailed @Bindable get() = state == STATE_LOADING_FAILED

    val loadingManual = ObservableBoolean(false)

    fun setLoading() {
        state = STATE_LOADING
    }

    fun setLoaded() {
        state = STATE_LOADED
    }

    fun setLoadingFailed() {
        state = STATE_LOADING_FAILED
    }

    companion object {
        private const val STATE_LOADING = 1
        private const val STATE_LOADED = 2
        private const val STATE_LOADING_FAILED = 3
    }
}