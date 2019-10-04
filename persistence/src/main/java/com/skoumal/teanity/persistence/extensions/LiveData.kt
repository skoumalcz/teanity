package com.skoumal.teanity.persistence.extensions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

/**
 * ## Definition
 * Returns currently stored value via [MutableLiveData.getValue], which is thread-safe. And sets
 * given value to the [MutableLiveData] via [MutableLiveData.postValue] which is also thread-safe.
 * */
var <T> MutableLiveData<T>.nextValue: T?
    get() = value
    set(value) = postValue(value!!)