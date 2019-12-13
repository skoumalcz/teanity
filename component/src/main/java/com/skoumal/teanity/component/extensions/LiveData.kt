package com.skoumal.teanity.component.extensions

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations

/** @see [Transformations.map] */
fun <T, R> LiveData<T>.map(mapper: (T) -> R): LiveData<R> =
    Transformations.map(this, mapper)

/** @see [Transformations.switchMap] */
fun <T, R> LiveData<T>.flatMap(mapper: (T) -> LiveData<R>): LiveData<R> =
    Transformations.switchMap(this, mapper)

/** @see [Transformations.distinctUntilChanged] */
fun <T> LiveData<T>.distinctUntilChanged(): LiveData<T> =
    Transformations.distinctUntilChanged(this)
