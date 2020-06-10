package com.skoumal.teanity.databinding

import androidx.databinding.ViewDataBinding
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter

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