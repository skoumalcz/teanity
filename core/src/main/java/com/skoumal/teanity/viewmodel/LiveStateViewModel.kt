package com.skoumal.teanity.viewmodel

import androidx.annotation.AnyThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.skoumal.teanity.tools.annotation.SubjectsToChange

/**
 * ## Definition
 *
 * [LiveStateViewModel] aims to reduce callbacks and endless UI pings by owning its [state] in one
 * central place. State is completely disconnected and should not contain callbacks of any kind.
 * */
@SubjectsToChange
abstract class LiveStateViewModel<State>(
    initialValue: State? = null
) : TeanityViewModel() {

    private val internalState = MutableLiveData<State>()
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
    val state: LiveData<State> = internalState

    init {
        internalState.value = initialValue
    }

    /**
     * ## Definition
     *
     * It's allowed to mutate state, so you are be able to prevent some state from being pushed
     * based on current external state.
     *
     * Common use-case would be preventing switching to "loaded" state whilst at the same time
     * having job running in the background.
     * */
    protected open fun mutateState(oldState: State, newState: State) = newState

    /**
     * ## Definition
     *
     * Pushes given state. Goes through [mutateState] to let you stop the current state before being
     * pushed to [internalState], and in-turn [state].
     * */
    @AnyThread
    protected fun State.publish() = mutateState(internalState.value ?: this, this)
        .let { internalState.postValue(it) }

}