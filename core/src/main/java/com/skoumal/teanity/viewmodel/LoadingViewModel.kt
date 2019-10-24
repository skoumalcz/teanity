package com.skoumal.teanity.viewmodel

import androidx.databinding.Bindable
import com.skoumal.teanity.BR
import kotlinx.coroutines.async

abstract class LoadingViewModel(defaultState: State = State.LOADING) :
    StatefulViewModel<LoadingViewModel.State>(defaultState) {

    val loading @Bindable get() = state == State.LOADING
    val loaded @Bindable get() = state == State.LOADED
    val loadingFailed @Bindable get() = state == State.LOADING_FAILED

    override suspend fun induceRefresh() = loading { super.induceRefresh() }

    override fun notifyStateChanged() {
        notifyPropertyChanged(BR.loading)
        notifyPropertyChanged(BR.loaded)
        notifyPropertyChanged(BR.loadingFailed)
    }

    inline fun loading(crossinline body: suspend () -> Unit) {
        async {
            state = State.LOADING
            body()
        }.invokeOnCompletion {
            state = if (it == null) State.LOADED else State.LOADING_FAILED
        }
    }

    enum class State {
        LOADED, LOADING, LOADING_FAILED
    }

}