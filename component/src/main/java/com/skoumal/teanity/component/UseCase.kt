/*
* Inspired by UseCase usage in Google IO Schedule app, licenced under Apache 2.0.
*
* Copyright 2018 Google LLC, 2019 Skoumal sro
* */

package com.skoumal.teanity.component

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.skoumal.teanity.component.extensions.distinctUntilChanged
import com.skoumal.teanity.tools.annotation.SubjectsToFutureChange
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * Allows for a testable, isolated business logic to be executed according with a given [dispatcher].
 *
 * In order to save time and resources when using UseCases in different screens, it gained internal
 * data designed to hold the last result. The result is however ignored whenever called with _own_
 * [MutableLiveData].
 *
 * # Important note:
 * [UseCase] will be transformed in [CompoundUseCase] in future versions specified by
 * [SubjectsToFutureChange].
 *
 * This will change behavior and return types of these methods:
 *
 * - [observe]
 * - [invoke]
 *
 * It will also remove following methods:
 *
 * - [now]
 *
 * Migrate to [CompoundUseCase] now, refactor optionally back to [UseCase] later.
 * */
@Suppress("MemberVisibilityCanBePrivate")
@SubjectsToFutureChange("2.0")
abstract class UseCase<in In, Out> {

    /**
     * ## Definition
     * Default dispatcher on which [execute] will be ran on.
     * */
    protected open val dispatcher = Dispatchers.Default

    private val data = provide()

    private val state = MutableLiveData<UseCaseState>(UseCaseState.IDLE)

    /**
     * ## Definition
     * Returns internal immutable [LiveData] to which result is supplied after calling [invoke]
     * without explicit [data] parameter.
     * */
    fun observe(): LiveData<ComponentResult<Out>> = data

    /**
     * ## Definition
     * Returns internal immutable [LiveData] with current state of this UseCase. By default the
     * state is defined as [UseCaseState.IDLE]. For all states and its descriptions see
     * [UseCaseState].
     *
     * ### Important notes
     * State is broadcasted in a distinct fashion - meaning it's impossible to receive two identical
     * consecutive values. For more information see [distinctUntilChanged].
     *
     * @see [UseCaseState]
     * @see [distinctUntilChanged]
     * */
    open fun observeState() = state.distinctUntilChanged()

    /**
     * ## Definition
     * Provides a result-typed [MutableLiveData].
     *
     * ### Notes
     * This might be useful when calling [invoke] with intent not to reuse preexisting [data].
     * The intended use might be as such:
     * ```
     * val exampleUseCase = ExampleUseCase(...)
     * val resultLiveData = exampleUseCase(..., exampleUseCase.provide())
     * ```
     * */
    fun provide() = MutableLiveData<ComponentResult<Out>>()

    /**
     * ## Definition
     * Provides immediate result if cached and starts execution logic defined in [execute].
     * */
    operator fun invoke(params: In): LiveData<ComponentResult<Out>> = invoke(params, data)

    /**
     * ## Definition
     * Starts execution login defined in [execute] and publishes result to the provided [data]
     * which it returns with weakened access for convenience.
     * */
    operator fun invoke(
        params: In,
        data: MutableLiveData<ComponentResult<Out>>
    ): LiveData<ComponentResult<Out>> =
        data.also { GlobalScope.launch(dispatcher) { now(params, it) } }

    /**
     * ## Definition
     * Starts execution logic in suspense on provided [dispatcher]. Sets up state of this use-case
     * (see [UseCaseState],[observeState]) and returns full result of the operation.
     *
     * ### Notes
     * Intended use-case for this method would be:
     *
     * 1) Requiring immediate result without waiting for [LiveData] to regain the result
     *
     * In particular:
     *
     * 1) Chaining use-cases
     * 2) Merging use-cases
     *
     * *This method needn't to be called explicitly.* Make sure you know what you're doing.
     * */
    @Synchronized
    suspend fun now(
        params: In,
        data: MutableLiveData<ComponentResult<Out>> = this.data
    ): ComponentResult<Out> {
        state.postValue(UseCaseState.LOADING)
        val result = catching { withContext(dispatcher) { execute(params) } }
            .also { data.postValue(it) }
        result.asPlatform()
            .onFailure { Timber.e(it) }
            .also { state.postValue(it.fold({ UseCaseState.IDLE }, { UseCaseState.FAILED })) }
        return result
    }

    /**
     * ## Definition
     * Overridable method designed to provide result to the business logic.
     * */
    protected abstract suspend fun execute(input: In): Out

}

@OptIn(SubjectsToFutureChange::class)
operator fun <R> UseCase<Unit, R>.invoke(): LiveData<ComponentResult<R>> =
    this(Unit)

@OptIn(SubjectsToFutureChange::class)
operator fun <R> UseCase<Unit, R>.invoke(result: MutableLiveData<ComponentResult<R>>) =
    this(Unit, result)