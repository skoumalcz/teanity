package com.skoumal.teanity.databinding

import androidx.annotation.CallSuper
import androidx.databinding.ViewDataBinding
import com.skoumal.teanity.BR
import com.skoumal.teanity.extensions.compareToSafe
import com.skoumal.teanity.util.ComparableEntity
import me.tatarka.bindingcollectionadapter2.ItemBinding

abstract class Item<out T> : GenericRvItem() {
    abstract val item: T

    override fun contentSameAs(other: GenericRvItem) =
        other.compareToSafe<Item<T>> { item == it.item }

    override fun sameAs(other: GenericRvItem) =
        other.compareToSafe<Item<T>> { item === it.item }
}

abstract class GenericRvItem : ComparableRvItem<GenericRvItem>()
abstract class ComparableRvItem<in T> : RvItem(), ComparableEntity<T>
abstract class RvItem {

    abstract val layoutRes: Int

    @CallSuper
    open fun bind(binding: ItemBinding<*>) {
        binding.set(BR.item, layoutRes)
    }

    /**
     * This callback is useful if you want to manipulate your views directly.
     * If you want to use this callback, you must set [me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter]
     * on your RecyclerView and call it from there. You can use [BindingBoundAdapter] for your convenience.
     */
    open fun onBindingBound(binding: ViewDataBinding) {}
}