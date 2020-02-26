package com.skoumal.teanity.viewmodel

import androidx.databinding.Bindable
import com.skoumal.teanity.BR
import com.skoumal.teanity.observable.observable
import kotlinx.coroutines.async

abstract class LoadingViewModel(defaultState: State = State.LOADING) : TeanityViewModel() {

    /**
     * Represents internal state of this viewModel and inherently attached view. It allows you to
     * monitor and change UI with the dependent state.
     *
     * Its state are defined within [State]. Those defined states will probably never change as the
     * granularity of 3 basic states suffices most use-cases. If you need a better control over
     * your states, you need to define your own viewModel. Handling multiple states within one
     * viewModel is strictly discouraged.
     *
     * @see loading
     * @see loaded
     * @see loadingFailed
     * */
    var state by observable(defaultState, BR.state, BR.loading, BR.loaded, BR.loadingFailed)
        @Bindable get

    /** Represents value of [state] that equals to [State.LOADING] */
    val loading @Bindable get() = state == State.LOADING
    /** Represents value of [state] that equals to [State.LOADED] */
    val loaded @Bindable get() = state == State.LOADED
    /** Represents value of [state] that equals to [State.LOADING_FAILED] */
    val loadingFailed @Bindable get() = state == State.LOADING_FAILED

    override suspend fun induceRefresh() = loading { super.induceRefresh() }

    /**
     * This function is a shorthand for calling `async {}` and managing the state yourself.
     *
     * On the start of internal execution (**not to confuse with call-site!**), it sets the state
     * to [State.LOADING]. The [body] is left to complete normally. If it throws exception the
     * state is set to [State.LOADING_FAILED], otherwise the viewModel will return to state
     * of [State.LOADED]
     *
     * ## Caution!
     *
     * If you consider failed execution as literally anything else than a crash (thrown exception),
     * **DO NOT** use this shorthand.
     *
     * You cannot set the internal [state] yourself here, because function will inevitably
     * overwrite your changes.
     * */
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