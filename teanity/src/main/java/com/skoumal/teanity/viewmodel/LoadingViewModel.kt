package com.skoumal.teanity.viewmodel

import androidx.databinding.Bindable
import com.skoumal.teanity.BR

abstract class LoadingViewModel : StatefulViewModel<LoadingViewModel.State>(State.LOADING) {

    val loading @Bindable get() = state == State.LOADING
    val loaded @Bindable get() = state == State.LOADED
    val loadingFailed @Bindable get() = state == State.LOADING_FAILED

    @Deprecated(
        "Direct access is recommended since 0.2. This access method will be removed in 1.0",
        ReplaceWith("state = State.LOADING", "com.skoumal.teanity.viewmodel.LoadingViewModel.State"),
        DeprecationLevel.WARNING
    )
    fun setLoading() {
        state = State.LOADING
    }

    @Deprecated(
        "Direct access is recommended since 0.2. This access method will be removed in 1.0",
        ReplaceWith("state = State.LOADED", "com.skoumal.teanity.viewmodel.LoadingViewModel.State"),
        DeprecationLevel.WARNING
    )
    fun setLoaded() {
        state = State.LOADED
    }

    @Deprecated(
        "Direct access is recommended since 0.2. This access method will be removed in 1.0",
        ReplaceWith("state = State.LOADING_FAILED", "com.skoumal.teanity.viewmodel.LoadingViewModel.State"),
        DeprecationLevel.WARNING
    )
    fun setLoadingFailed() {
        state = State.LOADING_FAILED
    }

    override fun notifyStateChanged() {
        notifyPropertyChanged(BR.loading)
        notifyPropertyChanged(BR.loaded)
        notifyPropertyChanged(BR.loadingFailed)
    }

    enum class State {
        LOADED, LOADING, LOADING_FAILED
    }
}