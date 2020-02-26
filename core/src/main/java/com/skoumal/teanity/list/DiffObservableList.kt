package com.skoumal.teanity.list

import android.util.Log
import androidx.annotation.AnyThread
import androidx.annotation.MainThread
import androidx.recyclerview.widget.DiffUtil
import com.skoumal.teanity.viewmodel.TeanityViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
        GlobalScope.launch(Dispatchers.Main) {
            val diff = withContext(Dispatchers.Default) {
                computeDiff(newItems)
            }
            update(newItems, diff)

        }
    }

    /**
     * Computes list diff on [Dispatchers.Default]. Requires call from [TeanityViewModel.launch] or similar.
     *
     * @param newItems  Future set of items
     * @return  [DiffUtil.DiffResult] that should be put into [update] right away. Contains all necessary info for
     * recycler to properly accommodate new items
     */
    suspend fun computeDiff(newItems: List<T>) = withContext(Dispatchers.Default) {
        val oldItems = synchronized(listLock) { list.toList() }
        doCalculateDiff(oldItems, newItems)
    }

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