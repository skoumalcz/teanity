/*
* Inspired by UseCase usage in Google IO Schedule app, licenced under Apache 2.0.
*
* Copyright 2018 Google LLC, 2019 Skoumal sro
* */

package com.skoumal.teanity.persistence

import androidx.lifecycle.LiveData

/**
 * Allows for a testable, isolated business logic.
 * */
@Suppress("MemberVisibilityCanBePrivate")
abstract class LiveUseCase<in In, Out> {

    /** Provides immediate result if cached and starts execution logic defined in [execute]. */
    operator fun invoke(params: In): LiveData<Out> = execute(params)

    /** Overridable method designed to provide result to the business logic. */
    protected abstract fun execute(input: In): LiveData<Out>

}

operator fun <R> LiveUseCase<Unit, R>.invoke(): LiveData<R> = this(Unit)