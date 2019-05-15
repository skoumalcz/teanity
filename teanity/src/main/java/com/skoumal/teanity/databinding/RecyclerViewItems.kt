package com.skoumal.teanity.databinding

import androidx.annotation.CallSuper
import androidx.databinding.ViewDataBinding
import com.skoumal.teanity.BR
import com.skoumal.teanity.util.ComparableEntity
import com.skoumal.teanity.util.DiffObservableList
import me.tatarka.bindingcollectionadapter2.ItemBinding
import me.tatarka.bindingcollectionadapter2.OnItemBind

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

//region Binding

inline fun <T : RvItem> bindingOf(crossinline init: (ItemBinding<*>) -> Unit) = OnItemBind<T> { itemBinding, _, item ->
    item.bind(itemBinding)
    init(itemBinding)
}

//endregion
//region DiffList

fun <T : ComparableEntity<T>> diffListOf() = DiffObservableList<T>(comparableCallback())
internal fun <T : ComparableEntity<T>> comparableCallback() = object : DiffObservableList.Callback<T> {
    override fun areContentsTheSame(oldItem: T, newItem: T) = oldItem.contentSameAs(newItem)
    override fun areItemsTheSame(oldItem: T, newItem: T) = oldItem.sameAs(newItem)
}

//endregion
//region Helper

inline fun <reified T> Any.compareToSafe(eval: (T) -> Boolean) = (this as? T)?.let(eval) ?: false

//endregion