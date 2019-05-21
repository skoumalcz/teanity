package com.skoumal.teanity.util

import androidx.recyclerview.widget.DiffUtil
import com.skoumal.teanity.extensions.applySchedulers
import com.skoumal.teanity.extensions.subscribeK
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject

/**
 * Default asynchronous implementation of [BaseDiffObservableList]. Execution of all diff calculations is strictly on
 * background thread.
 *
 * When populating list with new values, the list will queue all computation currently running and **will**
 * consecutively dispatch previous changes to listeners.
 *
 * In case of an error the queue will be emptied and re-started.
 *
 * @see BaseDiffObservableList
 * @inherit [BaseDiffObservableList]
 * */
open class AsyncDiffObservableList<T>(
    callback: Callback<T>,
    detectMoves: Boolean = true
) : BaseDiffObservableList<T>(callback, detectMoves) {

    private var dispatchQueue = PublishSubject.create<Single<Pair<List<T>, DiffUtil.DiffResult>>>()
    private var pendingList = mutableListOf<T>()

    init {
        dispatchQueue
            .onErrorResumeNext { throwable: Throwable ->
                throwable.printStackTrace()
                dispatchQueue = PublishSubject.create()
                dispatchQueue
            }
            .flatMapSingle { it }
            .subscribeK()
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun awaitDifferenceFrom(
        ignored: List<T>,
        newItems: List<T>,
        callback: (Pair<List<T>, DiffUtil.DiffResult>) -> Unit
    ) {
        val pending = synchronized(listLock) { pendingList.toList() }
        val task = Single.just(pending)
            .map {
                val old = synchronized(listLock) { list.toList() }
                it to doCalculateDiff(old, it)
            }
            .applySchedulers()
            .doOnSuccess(callback)
        dispatchQueue.onNext(task)
    }

    override fun add(element: T): Boolean {
        val result = pendingList.add(element)
        update(pendingList)
        return result
    }

    override fun add(index: Int, element: T) {
        pendingList.add(index, element)
        update(pendingList)
    }

    override fun addAll(elements: Collection<T>): Boolean {
        val result = pendingList.addAll(elements)
        update(pendingList)
        return result
    }

    override fun addAll(index: Int, elements: Collection<T>): Boolean {
        val result = pendingList.addAll(index, elements)
        update(pendingList)
        return result
    }

    override fun clear() {
        pendingList.clear()
        update(pendingList)
    }

    override fun remove(element: T): Boolean {
        val result = pendingList.remove(element)
        update(pendingList)
        return result
    }

    override fun removeAt(index: Int): T {
        val result = pendingList.removeAt(index)
        update(pendingList)
        return result
    }

    override fun set(index: Int, element: T): T {
        val result = pendingList.set(index, element)
        update(pendingList)
        return result
    }

}