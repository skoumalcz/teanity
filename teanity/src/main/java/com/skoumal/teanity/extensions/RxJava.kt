package com.skoumal.teanity.extensions

import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun <T> Observable<T>.applySchedulers(
    subscribeOn: Scheduler = Schedulers.io(),
    observeOn: Scheduler = AndroidSchedulers.mainThread()
): Observable<T> = this.subscribeOn(subscribeOn).observeOn(observeOn)

fun <T> Flowable<T>.applySchedulers(
    subscribeOn: Scheduler = Schedulers.io(),
    observeOn: Scheduler = AndroidSchedulers.mainThread()
): Flowable<T> = this.subscribeOn(subscribeOn).observeOn(observeOn)

fun <T> Single<T>.applySchedulers(
    subscribeOn: Scheduler = Schedulers.io(),
    observeOn: Scheduler = AndroidSchedulers.mainThread()
): Single<T> = this.subscribeOn(subscribeOn).observeOn(observeOn)

fun <T> Maybe<T>.applySchedulers(
    subscribeOn: Scheduler = Schedulers.io(),
    observeOn: Scheduler = AndroidSchedulers.mainThread()
): Maybe<T> = this.subscribeOn(subscribeOn).observeOn(observeOn)

fun Completable.applySchedulers(
    subscribeOn: Scheduler = Schedulers.io(),
    observeOn: Scheduler = AndroidSchedulers.mainThread()
): Completable = this.subscribeOn(subscribeOn).observeOn(observeOn)