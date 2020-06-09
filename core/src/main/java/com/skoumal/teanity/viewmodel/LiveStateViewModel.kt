package com.skoumal.teanity.viewmodel

import androidx.annotation.AnyThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * ## Definition
 *
 * [LiveStateViewModel] aims to reduce callbacks and endless UI pings by owning its [state] in one
 * central place. State is completely disconnected and should not contain callbacks of any kind.
 * */
abstract class LiveStateViewModel<State : Any>(
    initialValue: State? = null
) : TeanityViewModel() {

    private var pendingRefreshJob: Job? = null
        set(value) {
            if (field?.isActive == true) {
                field?.cancel()
            }
            field = value
        }

    private val internalState = MutableLiveData<State?>(null)
    private val internalIsLoading = MutableLiveData(false)

    /**
     * ## Definition
     *
     * It represents immutable internal state of this ViewModel's [State]. [State], by design,
     * should always be a data class, to allow easy mutation and updates to your state.
     *
     * ## Notes
     *
     * It always starts as `null` unless [LiveStateViewModel] has initialValue set in its
     * constructor. Make sure to update your state on `init {}` of your viewModel.
     *
     * We strongly suggest to create:
     *
     * ```kotlin
     * data class OurViewState(...) {
     *
     *      companion object {
     *          val default = OurViewState(...)
     *      }
     *
     * }
     * ```
     *
     * And reference it in constructor like so:
     *
     * ```kotlin
     * class OurViewModel: LiveStateViewModel(OurViewState.default)
     * ```
     *
     * In order to be as versatile as possible, we do not support creating the class by reflection
     * by the library. You can however do that with relative ease by yourself. Very simple example
     * of doing such thing might be:
     *
     * ```kotlin
     * abstract class OurStateViewModel(
     *      klass: KClass<*>
     * ): LiveStateViewModel(klass.java.newInstance())
     * ```
     * */
    @Suppress("MemberVisibilityCanBePrivate")
    val state: LiveData<State?> = internalState

    /**
     * ## Definition
     *
     * Provides information about whether the state is being loaded or is already loaded.
     * */
    val isLoading: LiveData<Boolean> = internalIsLoading

    init {
        initialValue?.publish()
    }

    /**
     * ## Definition
     *
     * It's allowed to mutate state, so you are be able to prevent some state from being pushed
     * based on current external state.
     *
     * Common use-case would be preventing switching to "loaded" state whilst at the same time
     * having job running in the background. Other possibility is to prepare new list's diff
     * callback.
     *
     * To throttle submissions of states and frequent UI updates you can simply delay the initial
     * execution of [mutateState] like so:
     *
     * ```
     * override fun mutateState(...) {
     *    delay(1000)
     *    ...
     * }
     * ```
     *
     * The viewModel will cancel current execution, as it's already running, and it will attempt to
     * start a new one. This process will repeat until the [publish] stops sending new states and
     * the state can be assigned to viewModel's [internalState].
     * */
    protected open suspend fun mutateState(
        oldState: State?,
        newState: State
    ) = newState

    /**
     * ## Definition
     *
     * Pushes given state. Goes through [mutateState] to let you stop the current state before being
     * pushed to [internalState], and in-turn [state].
     * */
    @AnyThread
    @JvmName(name = "publishState")
    protected fun State.publish() {
        pendingRefreshJob = viewModelScope.launch {
            internalIsLoading.postValue(true)
            mutateState(internalState.value, this@publish).also {
                internalState.postValue(it)
            }
            internalIsLoading.postValue(false)
        }
    }

}