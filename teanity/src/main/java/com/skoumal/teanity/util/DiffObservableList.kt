package com.skoumal.teanity.util

import androidx.annotation.MainThread
import androidx.recyclerview.widget.DiffUtil

/**
 * Default synchronous implementation of [BaseDiffObservableList]. Execution of all diff calculations is strictly on
 * main thread. This may impose some threats to seamlessness of the way users experience your UI.
 *
 * In case you use large lists consider using [AsyncDiffObservableList] which is calculating all differences on a
 * background thread.
 *
 * @see BaseDiffObservableList
 * @inherit [BaseDiffObservableList]
 * */
open class DiffObservableList<T>(
    callback: Callback<T>,
    detectMoves: Boolean = true
) : BaseDiffObservableList<T>(callback, detectMoves) {


    /**
     * Updates the contents of this list to the given one using the DiffResults to dispatch change
     * notifications.
     *
     * @param newItems   The items to set this list to.
     * @param diffResult The diff results to dispatch change notifications.
     */
    @MainThread
    fun update(newItems: List<T>, diffResult: DiffUtil.DiffResult) {
        updateSelfFrom(newItems, diffResult)
    }

    override fun awaitDifferenceFrom(oldItems: List<T>, newItems: List<T>, callback: (DiffUtil.DiffResult) -> Unit) {
        callback(doCalculateDiff(oldItems, newItems))
    }

}