/*
* Inspired by UseCase usage in Google IO Schedule app, licenced under Apache 2.0.
*
* Copyright 2018 Google LLC, 2019 Skoumal sro
* */

package com.skoumal.teanity.persistence

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Allows for a testable, isolated business logic to be executed according with a given [dispatcher].
 *
 * In order to save time and resources when using UseCases in different screens, it gained internal
 * data designed to hold the last result. The result is however ignored whenever called with _own_
 * [MutableLiveData].
 * */
@Suppress("MemberVisibilityCanBePrivate")
abstract class UseCase<in In, Out> {

    /** Default dispatcher on which [execute] will be ran on. */
    protected open val dispatcher = Dispatchers.Default

    private val data = MutableLiveData<Result<Out>>()

    fun observe(): LiveData<Result<Out>> = data

    /** Provides immediate result if cached and starts execution logic defined in [execute]. */
    operator fun invoke(params: In): LiveData<Result<Out>> = data.apply { invoke(params, this) }

    /** Starts execution login defined in [execute] and publishes result to the provided [data]. */
    operator fun invoke(params: In, data: MutableLiveData<Result<Out>>) {
        GlobalScope.launch(dispatcher) { now(params, data) }
    }

    /** Starts execution logic in suspense on provided [dispatcher] */
    suspend fun now(
        params: In,
        data: MutableLiveData<Result<Out>> = this.data
    ) {
        withContext(dispatcher) {
            runCatching { execute(params) }.also { data.postValue(it) }
        }
    }

    /** Overridable method designed to provide result to the business logic. */
    abstract suspend fun execute(input: In): Out

}

operator fun <R> UseCase<Unit, R>.invoke(): LiveData<Result<R>> = this(Unit)
operator fun <R> UseCase<Unit, R>.invoke(result: MutableLiveData<Result<R>>) = this(Unit, result)