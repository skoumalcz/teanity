package com.skoumal.teanity.extensions

import androidx.recyclerview.widget.DiffUtil
import com.skoumal.teanity.BR
import com.skoumal.teanity.databinding.RvItem
import com.skoumal.teanity.list.CoroutineBoundaryCallback
import com.skoumal.teanity.util.ComparableEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.tatarka.bindingcollectionadapter2.ItemBinding
import me.tatarka.bindingcollectionadapter2.OnItemBind
import me.tatarka.bindingcollectionadapter2.collections.DiffObservableList
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

//region Binding

inline fun <T : RvItem> bindingOf(
    noinline errorItem: (() -> Int)? = null,
    crossinline init: (ItemBinding<*>) -> Unit
) = OnItemBind<T> { itemBinding, _, item ->
    item?.bind(itemBinding)
        ?: errorItem?.let { itemBinding.set(BR.item, it.invoke()) }
        ?: throw IllegalStateException("You need to set error item when list items are null")
    init(itemBinding)
}

//endregion
//region DiffList

fun <T : ComparableEntity<T>> diffListOf() = DiffObservableList<T>(comparableCallback())
fun <T : ComparableEntity<T>> comparableCallback() = object : DiffUtil.ItemCallback<T>() {
    override fun areContentsTheSame(oldItem: T, newItem: T) = oldItem.contentSameAs(newItem)
    override fun areItemsTheSame(oldItem: T, newItem: T) = oldItem.sameAs(newItem)
}

//endregion
//region Helper

inline fun <reified T> Any.compareToSafe(eval: (T) -> Boolean) = (this as? T)?.let(eval) ?: false

/**
 * Uses coroutines to wrap calculating diff asynchronously. It uses [Dispatchers.Default] context
 * by default, but can be changed as per your choosing
 * */
suspend fun <T> DiffObservableList<T>.calculateDiffAsync(
    newList: List<T>,
    context: CoroutineContext = Dispatchers.Default
) = withContext(context) {
    calculateDiff(newList)
}

/**
 * Shorthand for anonymous boundary callback. Prefer, however, the full class implementation in all
 * cases. This is not considered pretty nor safe usage of callbacks.
 * */
fun <T : ComparableEntity<T>> boundaryCallbackOf(
    scope: CoroutineScope,
    dispatcher: CoroutineContext = EmptyCoroutineContext,
    loadStart: (suspend () -> Unit)? = null,
    loadEnd: (suspend (endItem: T) -> Unit)? = null
) = object : CoroutineBoundaryCallback<T>(scope, dispatcher) {
    override suspend fun onItemAtStartLoadRequested() = loadStart?.invoke() ?: Unit
    override suspend fun onItemAtEndLoadRequested(item: T) = loadEnd?.invoke(item) ?: Unit
}

//endregion