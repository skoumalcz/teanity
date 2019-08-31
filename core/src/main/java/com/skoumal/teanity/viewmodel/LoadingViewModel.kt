package com.skoumal.teanity.viewmodel

import androidx.databinding.Bindable
import com.skoumal.teanity.BR
import io.reactivex.*

abstract class LoadingViewModel(defaultState: State = State.LOADING) :
    StatefulViewModel<LoadingViewModel.State>(defaultState) {

    val loading @Bindable get() = state == State.LOADING
    val loaded @Bindable get() = state == State.LOADED
    val loadingFailed @Bindable get() = state == State.LOADING_FAILED

    override suspend fun induceRefresh() {
        if (lastRefresh == 0L) {
            state = State.LOADING
        }
        super.induceRefresh()
        state = State.LOADED
    }

    override fun notifyStateChanged() {
        notifyPropertyChanged(BR.loading)
        notifyPropertyChanged(BR.loaded)
        notifyPropertyChanged(BR.loadingFailed)
    }

    enum class State {
        LOADED, LOADING, LOADING_FAILED
    }

    //region Rx
    protected fun <T> Observable<T>.applyViewModel(viewModel: LoadingViewModel, allowFinishing: Boolean = true) =
        doOnSubscribe { viewModel.state = State.LOADING }
            .doOnError { viewModel.state = State.LOADING_FAILED }
            .doOnNext { if (allowFinishing) viewModel.state = State.LOADED }

    protected fun <T> Single<T>.applyViewModel(viewModel: LoadingViewModel, allowFinishing: Boolean = true) =
        doOnSubscribe { viewModel.state = State.LOADING }
            .doOnError { viewModel.state = State.LOADING_FAILED }
            .doOnSuccess { if (allowFinishing) viewModel.state = State.LOADED }

    protected fun <T> Maybe<T>.applyViewModel(viewModel: LoadingViewModel, allowFinishing: Boolean = true) =
        doOnSubscribe { viewModel.state = State.LOADING }
            .doOnError { viewModel.state = State.LOADING_FAILED }
            .doOnComplete { if (allowFinishing) viewModel.state = State.LOADED }
            .doOnSuccess { if (allowFinishing) viewModel.state = State.LOADED }

    protected fun <T> Flowable<T>.applyViewModel(viewModel: LoadingViewModel, allowFinishing: Boolean = true) =
        doOnSubscribe { viewModel.state = State.LOADING }
            .doOnError { viewModel.state = State.LOADING_FAILED }
            .doOnNext { if (allowFinishing) viewModel.state = State.LOADED }

    protected fun Completable.applyViewModel(viewModel: LoadingViewModel, allowFinishing: Boolean = true) =
        doOnSubscribe { viewModel.state = State.LOADING }
            .doOnError { viewModel.state = State.LOADING_FAILED }
            .doOnComplete { if (allowFinishing) viewModel.state = State.LOADED }
    //endregion
}