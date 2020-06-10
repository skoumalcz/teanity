package com.skoumal.teanity.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.skoumal.teanity.databinding.RecyclerViewItem
import java.util.*

open class BindingAdapter<Item : RecyclerViewItem>(
    private val extrasBinder: OnBindExtrasCallback? = null
) : RecyclerView.Adapter<BindingAdapter<Item>.ViewHolder>() {

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

    open fun callbackFrom(items: List<Item>) = object : DiffUtil.Callback() {

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

    @Synchronized
    open fun update(list: List<Item>, diff: DiffUtil.DiffResult) {
        internalItems = list.toMutableList()
        diff.dispatchUpdatesTo(this)
    }

    protected fun <E> List<E>.toImmutableList(): List<E> {
        return Collections.unmodifiableList(this)
    }

    open inner class ViewHolder(
        parent: ViewGroup,
        layoutRes: Int
    ) : RecyclerView.ViewHolder(
        DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            layoutRes,
            parent,
            false
        ).root
    ) {

        open fun bind(item: Item) {
            DataBindingUtil.bind<ViewDataBinding>(itemView)?.also {
                extrasBinder?.invoke(it)
                item.onBindingBound(it)
            }
        }

    }

}

typealias OnBindExtrasCallback = (ViewDataBinding) -> Unit



