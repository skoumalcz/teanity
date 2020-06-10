package com.skoumal.teanity.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.skoumal.teanity.databinding.RecyclerViewItem
import com.skoumal.teanity.extensions.calculateDiff
import java.lang.ref.WeakReference
import java.util.*

open class BindingAdapter<Item : RecyclerViewItem>(
    lifecycleOwner: LifecycleOwner? = null,
    private val extrasBinder: OnBindExtrasCallback? = null
) : RecyclerView.Adapter<BindingAdapter<Item>.ViewHolder>() {

    private var lifecycleOwner = lifecycleOwner?.let { WeakReference(it) }

    protected var internalItems = mutableListOf<Item>()
    val items get() = internalItems.toImmutableList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = ViewHolder(parent, viewType)

    override fun onBindViewHolder(
        holder: BindingAdapter<Item>.ViewHolder,
        position: Int
    ) = holder.bind(internalItems[position])

    override fun getItemViewType(
        position: Int
    ) = internalItems[position].layoutRes

    override fun getItemCount() = internalItems.size

    /**
     * ## Definition
     *
     * Overwrites lifecycle owner that is currently set to this adapter in a WeakReference fashion.
     * All views generated **after** the [lifecycleOwner] has been set, will have this exact [owner]
     * appended to them.
     *
     * Note that you don't have to set the owner this way, it's also available in the constructor -
     * in case you decide that you want to hold your data hostage in your views ¬‿¬.
     * */
    fun setLifecycleOwner(owner: LifecycleOwner?) {
        lifecycleOwner = if (owner == null) {
            lifecycleOwner?.clear()
            null
        } else {
            WeakReference(owner)
        }
    }

    /**
     * ## Definition
     *
     * Creates anonymous single-use callback for computing diff between currently set data and data
     * that will be potentially set to this adapter. Don't forget to call [calculateDiff] to
     * asynchronously compute the diff.
     *
     * The callback, at the moment of its birth, creates a frozen, unmodifiable, list from the
     * [source] and the [items] which get compared using [calculateDiff] extension method.
     * (Or using [DiffUtil] static methods, of course)
     * */
    open fun getCallbackFrom(source: List<Item>) = object : DiffUtil.Callback() {

        private val oldList = internalItems.toImmutableList()
        private val newList = items.toImmutableList()

        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].sameAs(newList[newItemPosition])
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].contentSameAs(newList[newItemPosition])
        }
    }

    /**
     * ## Definition
     *
     * Updates the [internalItems] with provided [list] and optionally [diff]. In case [diff] is not
     * provided, the adapter has immediately issued [notifyDataSetChanged].
     *
     * ## Warning
     *
     * Calling update too many times could result in crashes or inconsistencies. It's a duty of the
     * caller to ensure this won't happen.
     * */
    @Synchronized
    open fun update(list: List<Item>, diff: DiffUtil.DiffResult? = null) {
        internalItems = list.toMutableList()
        diff?.dispatchUpdatesTo(this) ?: notifyDataSetChanged()
    }

    protected fun <E> List<E>.toImmutableList(): List<E> {
        return Collections.unmodifiableList(this)
    }

    open inner class ViewHolder(
        parent: ViewGroup,
        layoutRes: Int
    ) : RecyclerView.ViewHolder(
        DataBindingUtil
            .inflate<ViewDataBinding>(parent.layoutInflater, layoutRes, parent, false)
            .also { it.lifecycleOwner = lifecycleOwner?.get() }
            .root
    ) {

        open fun bind(item: Item) {
            DataBindingUtil.bind<ViewDataBinding>(itemView)?.also {
                extrasBinder?.invoke(it)
                item.onBindingBound(it)
            }
        }

    }

    companion object {
        protected val View.layoutInflater get() = LayoutInflater.from(context)!!
    }


}

typealias OnBindExtrasCallback = (ViewDataBinding) -> Unit



