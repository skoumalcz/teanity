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
    open fun genericItemSameAs(other: Any): Boolean = other::class == this::class && itemSameAs(other as T)
    open fun genericContentSameAs(other: Any): Boolean = other::class == this::class && contentSameAs(other as T)

    companion object {
        val callback = object : DiffObservableList.Callback<ComparableRvItem<*>> {
            override fun areItemsTheSame(
                oldItem: ComparableRvItem<*>,
                newItem: ComparableRvItem<*>
            ) = oldItem.genericItemSameAs(newItem)

            override fun areContentsTheSame(
                oldItem: ComparableRvItem<*>,
                newItem: ComparableRvItem<*>
            ) = newItem.genericContentSameAs(newItem)
        }
    }
}