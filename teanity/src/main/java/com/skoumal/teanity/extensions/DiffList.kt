package com.skoumal.teanity.extensions

import com.skoumal.teanity.databinding.RvItem
import com.skoumal.teanity.util.AsyncDiffObservableList
import com.skoumal.teanity.util.BaseDiffObservableList
import com.skoumal.teanity.util.ComparableEntity
import com.skoumal.teanity.util.DiffObservableList
import me.tatarka.bindingcollectionadapter2.ItemBinding
import me.tatarka.bindingcollectionadapter2.OnItemBind

//region Binding

inline fun <T : RvItem> bindingOf(crossinline init: (ItemBinding<*>) -> Unit) = OnItemBind<T> { itemBinding, _, item ->
    item.bind(itemBinding)
    init(itemBinding)
}

//endregion
//region DiffList

fun <T : ComparableEntity<T>> diffListOf() = DiffObservableList<T>(comparableCallback())
fun <T : ComparableEntity<T>> asyncListOf() = AsyncDiffObservableList<T>(comparableCallback())
internal fun <T : ComparableEntity<T>> comparableCallback() = object : BaseDiffObservableList.Callback<T> {
    override fun areContentsTheSame(oldItem: T, newItem: T) = oldItem.contentSameAs(newItem)
    override fun areItemsTheSame(oldItem: T, newItem: T) = oldItem.sameAs(newItem)
}

//endregion
//region Helper

inline fun <reified T> Any.compareToSafe(eval: (T) -> Boolean) = (this as? T)?.let(eval) ?: false

//endregion