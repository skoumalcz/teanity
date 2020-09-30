/*
* Inspired by UseCase usage in Google IO Schedule app, licenced under Apache 2.0.
*
* Copyright 2018 Google LLC, 2019 Skoumal sro
* */

package com.skoumal.teanity.component

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.skoumal.teanity.component.UseCaseState.*
import com.skoumal.teanity.component.extensions.distinctUntilChanged
import com.skoumal.teanity.component.extensions.map
import com.skoumal.teanity.tools.log.error
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Allows for a testable, isolated business logic to be executed according with a given [dispatcher].
 *
 * In order to save time and resources when using UseCases in different screens, it gained internal
 * data designed to hold the last ComponentResult. The ComponentResult is however ignored whenever called with _own_
 * [MutableLiveData].
 * */
@Suppress("MemberVisibilityCanBePrivate")
abstract class CompoundUseCase<in In, Out> {

    /**
     * ## Definition
     * Default dispatcher on which [execute] will be ran on.
     * */
    protected open val dispatcher = Dispatchers.Default

    private val data = provide()

    private val state = MutableLiveData<UseCaseState>(IDLE)

    /**
     * ## Definition
     * By default returns internal [LiveData] which is updated on every [invoke] call.
     *
     * It can be overridden and it's functionality might replaced with fetching data from Room or
     * other observable source.
     * */
    open fun observe(input: In): LiveData<Out?> {
        GlobalScope.launch(Dispatchers.Unconfined) { invoke(input) }
        return data.map { it.getOrNull() }
    }

    /**
     * ## Definition
     * Returns internal immutable [LiveData] to which result is supplied after calling [invoke]
     * without explicit [data] parameter.
     * */
    fun observeData(): LiveData<ComponentResult<Out>> = data

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
    suspend operator fun invoke(params: In): ComponentResult<Out> = invoke(params, data)

    /**
     * ## Definition
     * Starts execution login defined in [execute] and publishes result to the provided [data]
     * which it returns with weakened access for convenience.
     * */
    @Synchronized
    suspend operator fun invoke(
        params: In,
        data: MutableLiveData<ComponentResult<Out>>
    ): ComponentResult<Out> {
        state.postValue(LOADING)
        return catching { withContext(dispatcher) { execute(params) } }
            .also {
                data.postValue(it)
                it.asPlatform().onFailure { err -> error(err) }
                state.postValue(it.fold({ IDLE }, { FAILED }))
            }
    }

    /**
     * ## Definition
     * Overridable method designed to provide result to the business logic.
     * */
    @Throws(Throwable::class)
    protected abstract suspend fun execute(input: In): Out

}

suspend operator fun <R> CompoundUseCase<Unit, R>.invoke(): ComponentResult<R> = this(Unit)
suspend operator fun <R> CompoundUseCase<Unit, R>.invoke(ComponentResult: MutableLiveData<ComponentResult<R>>) =
    this(Unit, ComponentResult)