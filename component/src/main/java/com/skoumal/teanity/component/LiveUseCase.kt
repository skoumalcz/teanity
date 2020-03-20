/*
* Inspired by UseCase usage in Google IO Schedule app, licenced under Apache 2.0.
*
* Copyright 2018 Google LLC, 2019 Skoumal sro
* */

package com.skoumal.teanity.component

import androidx.lifecycle.LiveData
import com.skoumal.teanity.tools.annotation.RemoveOnDeprecation

/**
 * Allows for a testable, isolated business logic.
 *
 * Designed for DAO's LiveData which are refreshed by Room's implementation. Calling [execute]
 * multiple times results in returning different [LiveData] objects and will not refresh the
 * initial one.
 *
 * Everything in [execute] is called in a synchronous fashion and the result of [execute] is passed
 * to [invoke] immediately.
 * */
@Suppress("MemberVisibilityCanBePrivate")
@RemoveOnDeprecation("2.0")
@Deprecated("Use CompoundUseCase with overridden >observe< method.")
abstract class LiveUseCase<in In, Out> {

    /** Provides immediate result if cached and starts execution logic defined in [execute]. */
    @Deprecated("Use CompoundUseCase with overridden >observe< method.")
    operator fun invoke(params: In): LiveData<Out> = execute(params)

    /** Overridable method designed to provide result to the business logic. */
    @Deprecated("Use CompoundUseCase with overridden >observe< method.")
    protected abstract fun execute(input: In): LiveData<Out>

}

@Deprecated("Use CompoundUseCase with overridden >observe< method.")
operator fun <R> LiveUseCase<Unit, R>.invoke(): LiveData<R> = this(Unit)