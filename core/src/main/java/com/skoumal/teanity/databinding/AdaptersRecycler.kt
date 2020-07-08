package com.skoumal.teanity.databinding

import android.graphics.drawable.Drawable
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.skoumal.teanity.extensions.drawableCompat

@BindingAdapter("dividerVertical", "dividerHorizontal", requireAll = false)
fun RecyclerView.setDividers(dividerVertical: Int, dividerHorizontal: Int) {
    val horizontal = if (dividerHorizontal > 0) {
        context.drawableCompat(dividerHorizontal)
    } else {
        null
    }
    val vertical = if (dividerVertical > 0) {
        context.drawableCompat(dividerVertical)
    } else {
        null
    }
    setDividers(vertical, horizontal)
}

@BindingAdapter("dividerVertical", "dividerHorizontal", requireAll = false)
fun RecyclerView.setDividers(dividerVertical: Drawable?, dividerHorizontal: Drawable?) {
    if (dividerHorizontal != null) {
        DividerItemDecoration(context, LinearLayoutManager.HORIZONTAL).apply {
            setDrawable(dividerHorizontal)
        }.let { addItemDecoration(it) }
    }
    if (dividerVertical != null) {
        DividerItemDecoration(context, LinearLayoutManager.VERTICAL).apply {
            setDrawable(dividerVertical)
        }.let { addItemDecoration(it) }
    }
}

@BindingAdapter("bindingAdapter")
fun RecyclerView.setAdapterWithLifecycleOwner(
    adapter: com.skoumal.teanity.list.BindingAdapter<*>
) {
    fun findLifecycleOwner() = DataBindingUtil
        .findBinding<ViewDataBinding>(this)
        ?.lifecycleOwner ?: context as? LifecycleOwner

    adapter.setLifecycleOwner(findLifecycleOwner())

    this.adapter = adapter
}