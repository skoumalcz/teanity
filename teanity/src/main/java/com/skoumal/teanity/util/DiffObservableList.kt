package com.skoumal.teanity.util

import android.util.Log
import androidx.annotation.AnyThread
import androidx.annotation.MainThread
import androidx.recyclerview.widget.DiffUtil
import com.skoumal.teanity.extensions.applySchedulers
import com.skoumal.teanity.extensions.subscribeK
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

/**
 * Default synchronous implementation of [BaseDiffObservableList]. Execution of all diff calculations by [update] is
 * strictly on main thread. This may impose some threats to seamlessness of the way users experience your UI. Consider
 * using [updateAsync] (one time updates) or [updateRx] (repeated batch updates).
 *
 * Using [update], [updateAsync] or [updateRx] is rather inefficient for adding/(re)moving singular items. Use direct
 * methods which update the list swiftly.
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

    /**
     * Updates the contents asynchronously with no feedback loop. If you need information about when the list is
     * finished updating, consider using [updateRx].
     *
     * Internal list won't be updated until the diff is calculated.
     *
     * @param newItems   The items to set this list to.
     * */
    @AnyThread
    fun updateAsync(newItems: List<T>) {
        updateRx(newItems).subscribeK()
    }

    /**
     * Updates the contents asynchronously with feedback loop.
     *
     * @param newItems   The items to set this list to.
     * @return [Single] that automatically calculates diff and applies it to internal list. [Single] is subscribed to
     * on [Schedulers.computation] thread and switched back to [Schedulers.io] when done
     * */
    @AnyThread
    fun updateRx(newItems: List<T>) = Single
        .fromCallable {
            val oldItems = synchronized(listLock) { list.toList() }
            newItems to doCalculateDiff(oldItems, newItems)
        }
        .applySchedulers(subscribeOn = Schedulers.computation())
        .doOnSuccess { update(it.first, it.second) }
        .applySchedulers(observeOn = Schedulers.io())
        .map { it.first }

    override fun awaitDifferenceFrom(
        oldItems: List<T>,
        newItems: List<T>,
        callback: (Pair<List<T>, DiffUtil.DiffResult>) -> Unit
    ) {
        if (oldItems.size > 10 || newItems.size > 10) {
            Log.e(
                "DiffObservableList",
                "Updating list with more than 10 items can affect your app's performance. " +
                        "Consider pre-calculating list diff on a background thread."
            )
        }
        callback(newItems to doCalculateDiff(oldItems, newItems))
    }

}