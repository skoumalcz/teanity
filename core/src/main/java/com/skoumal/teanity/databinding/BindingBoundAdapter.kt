package com.skoumal.teanity.databinding

import androidx.databinding.ViewDataBinding
import com.skoumal.teanity.tools.annotation.RemoveOnDeprecation
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter

@Deprecated(
    "Use BindingAdapter without explicit ItemBinding instead",
    replaceWith = ReplaceWith("BindingAdapter", "com.skoumal.teanity.list.BindingAdapter")
)
@RemoveOnDeprecation("1.3")
open class BindingBoundAdapter : BindingRecyclerViewAdapter<RecyclerViewItem>() {

    override fun onBindBinding(
        binding: ViewDataBinding,
        variableId: Int,
        layoutRes: Int,
        position: Int,
        item: RecyclerViewItem
    ) {
        super.onBindBinding(binding, variableId, layoutRes, position, item)

        item.onBindingBound(binding)
    }
}