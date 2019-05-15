package com.skoumal.teanity.util

import androidx.recyclerview.widget.DiffUtil
import com.skoumal.teanity.extensions.subscribeK
import io.reactivex.Single
import io.reactivex.disposables.Disposable

/**
 * Default asynchronous implementation of [BaseDiffObservableList]. Execution of all diff calculations is strictly on
 * background thread.
 *
 * When populating list with new values, the list will scrap all computation currently running and **will not**
 * dispatch previous changes to listeners. This is done in order to minimize number of animations running at once when
 * list is getting rapidly updated.
 *
 * @see BaseDiffObservableList
 * @inherit [BaseDiffObservableList]
 * */
open class AsyncDiffObservableList<T>(
    callback: Callback<T>,
    detectMoves: Boolean = true
) : BaseDiffObservableList<T>(callback, detectMoves) {

    private var runningTask: Disposable? = null

    val isPendingChange get() = runningTask?.isDisposed?.not() ?: false

    override fun awaitDifferenceFrom(oldItems: List<T>, newItems: List<T>, callback: (DiffUtil.DiffResult) -> Unit) {
        runningTask?.dispose()
        runningTask = Single.just(oldItems to newItems)
            .map { doCalculateDiff(oldItems, newItems) }
            .subscribeK { callback(it) }
    }
}