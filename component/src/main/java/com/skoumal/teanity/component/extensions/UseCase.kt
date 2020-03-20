package com.skoumal.teanity.component.extensions

import com.skoumal.teanity.component.UseCase
import com.skoumal.teanity.component.UseCaseState
import com.skoumal.teanity.tools.annotation.SubjectsToFutureChange

/**
 * ## Definition
 * Represents boolean state of given useCase. It uses [UseCase.observeState] and [map] to check
 * whether the current state matches the required one.
 * */
@OptIn(SubjectsToFutureChange::class)
fun UseCase<*, *>.isLoading() =
    observeState().map { it == UseCaseState.LOADING }.distinctUntilChanged()

/** @see [isLoading] */
@OptIn(SubjectsToFutureChange::class)
fun UseCase<*, *>.isIdle() =
    observeState().map { it == UseCaseState.IDLE }.distinctUntilChanged()

/** @see [isLoading] */
@OptIn(SubjectsToFutureChange::class)
fun UseCase<*, *>.isFailed() =
    observeState().map { it == UseCaseState.FAILED }.distinctUntilChanged()