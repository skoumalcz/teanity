package com.skoumal.teanity.databinding

import androidx.annotation.CallSuper
import androidx.databinding.ObservableList
import androidx.databinding.ViewDataBinding
import com.skoumal.teanity.BR
import com.skoumal.teanity.extensions.bindingOf
import com.skoumal.teanity.list.BindingAdapter
import com.skoumal.teanity.tools.annotation.RemoveOnDeprecation
import com.skoumal.teanity.util.ComparableEntity
import me.tatarka.bindingcollectionadapter2.ItemBinding

@Suppress("DEPRECATION")
@Deprecated("Use RecyclerViewItem", ReplaceWith("RecyclerViewItem"))
@RemoveOnDeprecation("1.3")
abstract class GenericRvItem : ComparableRvItem<GenericRvItem>()

@Suppress("DEPRECATION")
@Deprecated("Use RecyclerViewItem", ReplaceWith("RecyclerViewItem"))
@RemoveOnDeprecation("1.3")
abstract class ComparableRvItem<in T> : RvItem()

@Deprecated("Use RecyclerViewItem", ReplaceWith("RecyclerViewItem"))
@RemoveOnDeprecation("1.3")
abstract class RvItem : RecyclerViewItem()

abstract class RecyclerViewItem : ComparableEntity<RecyclerViewItem> {

    abstract val layoutRes: Int

    /**
     * ## Definition
     *
     * Sets layout to the [BR.item] variable. It's invoked by [bindingOf] exclusively. You can call
     * it yourself in the case when you're using [ObservableList] in conjunction with [ItemBinding].
     * */
    @CallSuper
    open fun bind(binding: ItemBinding<*>) {
        binding.set(BR.item, layoutRes)
    }

    /**
     * ## Definition
     *
     * Pending bindings are immediately executed upon invoking `super.onBindingBound()`. This
     * function is invoked exclusively by [BindingAdapter].
     *
     * # Warning
     *
     * If you plan to add additional variables and other binding magic, make sure to call `super` as
     * the last statement!
     * */
    @CallSuper
    open fun onBindingBound(binding: ViewDataBinding) {
        binding.setVariable(BR.item, this)
        binding.executePendingBindings()
    }

}