package com.skoumal.teanity.component.extensions

import com.skoumal.teanity.component.UseCase
import com.skoumal.teanity.component.UseCaseState

/**
 * ## Definition
 * Represents boolean state of given useCase. It uses [UseCase.observeState] and [map] to check
 * whether the current state matches the required one.
 * */
fun UseCase<*, *>.isLoading() =
    observeState().map { it == UseCaseState.LOADING }

/** @see [isLoading] */
fun UseCase<*, *>.isIdle() =
    observeState().map { it == UseCaseState.IDLE }

/** @see [isLoading] */
fun UseCase<*, *>.isFailed() =
    observeState().map { it == UseCaseState.FAILED }