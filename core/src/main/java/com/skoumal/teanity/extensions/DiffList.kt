package com.skoumal.teanity.extensions

import androidx.recyclerview.widget.DiffUtil
import com.skoumal.teanity.BR
import com.skoumal.teanity.databinding.RecyclerViewItem
import com.skoumal.teanity.list.CoroutineBoundaryCallback
import com.skoumal.teanity.tools.annotation.RemoveOnDeprecation
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

@Deprecated(
    "It's no longer suggested to use 3rd party libraries for binding to lists",
    ReplaceWith("BindingAdapter", "com.skoumal.teanity.list.BindingAdapter"),
    level = DeprecationLevel.WARNING
)
@RemoveOnDeprecation("1.3")
inline fun <T : RecyclerViewItem> bindingOf(
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

@Suppress("DeprecatedCallableAddReplaceWith")
@Deprecated(
    "It's no longer suggested to use 3rd party libraries for binding to lists. See com.skoumal.teanity.list.BindingAdapter and com.skoumal.teanity.viewmodel.LiveStateViewModel",
    level = DeprecationLevel.WARNING
)
@RemoveOnDeprecation("1.3")
fun <T : ComparableEntity<T>> diffListOf() = DiffObservableList<T>(comparableCallback())

@Suppress("DeprecatedCallableAddReplaceWith")
@Deprecated(
    "It's no longer suggested to use 3rd party libraries for binding to lists. See com.skoumal.teanity.list.BindingAdapter and com.skoumal.teanity.viewmodel.LiveStateViewModel",
    level = DeprecationLevel.WARNING
)
@RemoveOnDeprecation("1.3")
fun <T : ComparableEntity<T>> comparableCallback() = object : DiffUtil.ItemCallback<T>() {
    override fun areContentsTheSame(oldItem: T, newItem: T) = oldItem.contentSameAs(newItem)
    override fun areItemsTheSame(oldItem: T, newItem: T) = oldItem.sameAs(newItem)
}

//endregion
//region Helper

@Deprecated(
    "Use language native compareTo",
    ReplaceWith("compareTo"),
    level = DeprecationLevel.ERROR
)
@RemoveOnDeprecation("1.3")
inline fun <reified T> Any.compareToSafe(eval: (T) -> Boolean) = compareTo(eval)
inline fun <reified T> Any.compareTo(eval: (T) -> Boolean) = (this as? T)?.let(eval) ?: false

/**
 * Uses coroutines to wrap calculating diff asynchronously. It uses [Dispatchers.Default] context
 * by default, but can be changed as per your choosing
 * */
@Suppress("DeprecatedCallableAddReplaceWith")
@Deprecated(
    "It's no longer suggested to use 3rd party libraries for binding to lists.",
    level = DeprecationLevel.ERROR
)
@RemoveOnDeprecation("1.3")
suspend fun <T> DiffObservableList<T>.calculateDiffAsync(
    newList: List<T>,
    context: CoroutineContext = Dispatchers.Default
) = withContext(context) {
    calculateDiff(newList)
}

/**
 * Calculates diff asynchronously on provided dispatcher.
 * Default to [Dispatchers.Default].
 * */
suspend inline fun DiffUtil.Callback.calculateDiff(
    dispatcher: CoroutineContext = Dispatchers.Default
) = withContext(dispatcher) {
    DiffUtil.calculateDiff(this@calculateDiff)
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