package com.skoumal.teanity.databinding

import android.support.annotation.CallSuper
import com.skoumal.teanity.BR
import com.skoumal.teanity.util.DiffObservableList
import me.tatarka.bindingcollectionadapter2.ItemBinding

abstract class RvItem {

    abstract val layoutRes: Int

    @CallSuper
    open fun bind(binding: ItemBinding<*>) {
        binding.set(BR.item, layoutRes)
    }
}

abstract class ComparableRvItem<in T> : RvItem() {

    abstract fun itemSameAs(other: T): Boolean
    abstract fun contentSameAs(other: T): Boolean

    companion object {
        fun callback(
            areItemsTheSame: (ComparableRvItem<*>, ComparableRvItem<*>) -> Boolean,
            areContentsTheSame: (ComparableRvItem<*>, ComparableRvItem<*>) -> Boolean
        ) = object : DiffObservableList.Callback<ComparableRvItem<*>> {
            override fun areItemsTheSame(
                oldItem: ComparableRvItem<*>,
                newItem: ComparableRvItem<*>
            ) = when {
                oldItem::class == newItem::class -> areItemsTheSame(oldItem, newItem)
                else -> false
            }

            override fun areContentsTheSame(
                oldItem: ComparableRvItem<*>,
                newItem: ComparableRvItem<*>
            ) = when {
                oldItem::class == newItem::class -> areContentsTheSame(oldItem, newItem)
                else -> false
            }
        }
    }
}