package com.skoumal.teanity.list

import androidx.annotation.MainThread
import androidx.databinding.ListChangeRegistry
import androidx.databinding.ObservableList
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import com.skoumal.teanity.tools.annotation.RemoveOnDeprecation
import java.util.*

/**
 * @param callback    The callback that controls the behavior of the DiffObservableList.
 * @param detectMoves True if DiffUtil should try to detect moved items, false otherwise.
 */
@Deprecated("Use first party DiffObservableList")
@RemoveOnDeprecation("1.2")
abstract class BaseDiffObservableList<T>(
    private val callback: Callback<T>,
    private val detectMoves: Boolean = true
) : AbstractList<T>(), ObservableList<T> {

    protected val listLock = Any()
    protected var list: MutableList<T> = mutableListOf()
    protected val listeners = ListChangeRegistry()
    protected val listCallback = ObservableListUpdateCallback()

    override val size: Int get() = list.size

    protected fun updateSelfFrom(newItems: List<T>, result: DiffUtil.DiffResult? = null) {
        val frozenOldList = synchronized(listLock) { list.toList() }
        val frozenNewList = synchronized(listLock) { newItems.toList() }

        fun updateSelf(newList: List<T>, result: DiffUtil.DiffResult) {
            synchronized(listLock) { list = newList.toMutableList() }
            synchronized(listCallback) { result.dispatchUpdatesTo(listCallback) }
        }

        result?.let { updateSelf(frozenNewList, it) } ?: awaitDifferenceFrom(frozenOldList, frozenNewList) {
            updateSelf(it.first, it.second)
        }
    }

    protected open fun doCalculateDiff(oldItems: List<T>, newItems: List<T>): DiffUtil.DiffResult {
        return DiffUtil.calculateDiff(object : DiffUtil.Callback() {

            override fun getOldListSize() = oldItems.size
            override fun getNewListSize() = newItems.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val oldItem = oldItems[oldItemPosition]
                val newItem = newItems[newItemPosition]
                return callback.areItemsTheSame(oldItem, newItem)
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val oldItem = oldItems[oldItemPosition]
                val newItem = newItems[newItemPosition]
                return callback.areContentsTheSame(oldItem, newItem)
            }
        }, detectMoves)
    }

    /**
     * Sets this list to the given items. Items are set instantly and difference between those lists is calculated
     * afterwards.
     *
     * Children (lists extended from this one) are free to implement whatever strategy they want for computing the diff.
     * Default implementation is available at [DiffObservableList]. Refer to documentation of each of those lists to
     * learn more about its strategy.
     *
     * @param newItems The items to set this list to.
     */
    @MainThread
    fun update(newItems: List<T>) = updateSelfFrom(newItems)

    override fun addOnListChangedCallback(listener: ObservableList.OnListChangedCallback<out ObservableList<T>>) {
        listeners.add(listener)
    }

    override fun removeOnListChangedCallback(listener: ObservableList.OnListChangedCallback<out ObservableList<T>>) {
        listeners.remove(listener)
    }

    override fun get(index: Int): T {
        return list[index]
    }

    override fun add(element: T): Boolean {
        list.add(element)
        notifyAdd(size - 1, 1)
        return true
    }

    override fun add(index: Int, element: T) {
        list.add(index, element)
        notifyAdd(index, 1)
    }

    override fun addAll(elements: Collection<T>): Boolean {
        val oldSize = size
        val added = list.addAll(elements)
        if (added) {
            notifyAdd(oldSize, size - oldSize)
        }
        return added
    }

    override fun addAll(index: Int, elements: Collection<T>): Boolean {
        val added = list.addAll(index, elements)
        if (added) {
            notifyAdd(index, elements.size)
        }
        return added
    }

    override fun clear() {
        val oldSize = size
        list.clear()
        if (oldSize != 0) {
            notifyRemove(0, oldSize)
        }
    }

    override fun remove(element: T): Boolean {
        val index = indexOf(element)
        return if (index >= 0) {
            removeAt(index)
            true
        } else {
            false
        }
    }

    override fun removeAt(index: Int): T {
        val element = list.removeAt(index)
        notifyRemove(index, 1)
        return element
    }

    fun removeLast(): T? {
        if (size > 0) {
            val index = size - 1
            return removeAt(index)
        }
        return null
    }

    override fun set(index: Int, element: T): T {
        val old = list.set(index, element)
        listeners.notifyChanged(this, index, 1)
        return old
    }

    private fun notifyAdd(start: Int, count: Int) {
        listeners.notifyInserted(this, start, count)
    }

    private fun notifyRemove(start: Int, count: Int) {
        listeners.notifyRemoved(this, start, count)
    }

    protected abstract fun awaitDifferenceFrom(
        oldItems: List<T>,
        newItems: List<T>,
        callback: (Pair<List<T>, DiffUtil.DiffResult>) -> Unit
    )

    /**
     * A Callback class used by DiffUtil while calculating the diff between two lists.
     */
    interface Callback<T> {
        /**
         * Called by the DiffUtil to decide whether two object represent the same Item.
         *
         *
         * For example, if your items have unique ids, this method should check their id equality.
         *
         * @param oldItem The old item.
         * @param newItem The new item.
         * @return True if the two items represent the same object or false if they are different.
         */
        fun areItemsTheSame(oldItem: T, newItem: T): Boolean

        /**
         * Called by the DiffUtil when it wants to check whether two items have the same data.
         * DiffUtil uses this information to detect if the contents of an item has changed.
         *
         *
         * DiffUtil uses this method to check equality instead of [Object.equals] so
         * that you can change its behavior depending on your UI.
         *
         *
         * This method is called only if [.areItemsTheSame] returns `true` for
         * these items.
         *
         * @param oldItem The old item.
         * @param newItem The new item which replaces the old item.
         * @return True if the contents of the items are the same or false if they are different.
         */
        fun areContentsTheSame(oldItem: T, newItem: T): Boolean
    }

    inner class ObservableListUpdateCallback : ListUpdateCallback {
        override fun onChanged(position: Int, count: Int, payload: Any?) {
            listeners.notifyChanged(this@BaseDiffObservableList, position, count)
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            listeners.notifyMoved(this@BaseDiffObservableList, fromPosition, toPosition, 1)
        }

        override fun onInserted(position: Int, count: Int) {
            modCount += 1
            listeners.notifyInserted(this@BaseDiffObservableList, position, count)
        }

        override fun onRemoved(position: Int, count: Int) {
            modCount += 1
            listeners.notifyRemoved(this@BaseDiffObservableList, position, count)
        }

    }

}