package com.skoumal.teanity.persistence.extensions

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations

fun <T, R> LiveData<T>.map(mapper: (T) -> R): LiveData<R> =
    Transformations.map(this, mapper)

fun <T, R> LiveData<T>.flatMap(mapper: (T) -> LiveData<R>): LiveData<R> =
    Transformations.switchMap(this, mapper)

fun <T> LiveData<T>.distinctUntilChanged(): LiveData<T> =
    Transformations.distinctUntilChanged(this)