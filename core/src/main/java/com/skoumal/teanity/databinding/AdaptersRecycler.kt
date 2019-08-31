package com.skoumal.teanity.databinding

import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.InsetDrawable
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.skoumal.teanity.extensions.drawableCompat
import com.skoumal.teanity.extensions.startEndToLeftRight
import com.skoumal.teanity.extensions.toPx
import com.skoumal.teanity.list.EndlessRecyclerScrollListener
import com.skoumal.teanity.util.KItemDecoration
import kotlin.math.roundToInt

@Deprecated("This is not a viable solution and lacks certain features.")
@BindingAdapter(
    "dividerColor",
    "dividerHorizontal",
    "dividerSize",
    "dividerAfterLast",
    "dividerMarginStart",
    "dividerMarginEnd",
    "dividerMarginTop",
    "dividerMarginBottom",
    requireAll = false
)
fun RecyclerView.setDivider(
    color: Int,
    horizontal: Boolean,
    _size: Float,
    _afterLast: Boolean?,
    marginStartF: Float,
    marginEndF: Float,
    marginTopF: Float,
    marginBottomF: Float
) {
    val orientation = if (horizontal) RecyclerView.HORIZONTAL else RecyclerView.VERTICAL
    val size = if (_size > 0) _size.roundToInt() else 1.toPx()
    val (width, height) = if (horizontal) size to 1 else 1 to size
    val afterLast = _afterLast ?: true

    val marginStart = marginStartF.roundToInt()
    val marginEnd = marginEndF.roundToInt()
    val marginTop = marginTopF.roundToInt()
    val marginBottom = marginBottomF.roundToInt()
    val (marginLeft, marginRight) = context.startEndToLeftRight(marginStart, marginEnd)

    val drawable = GradientDrawable().apply {
        setSize(width, height)
        shape = GradientDrawable.RECTANGLE
        setColor(color)
    }.let {
        InsetDrawable(it, marginLeft, marginTop, marginRight, marginBottom)
    }

    val decoration = KItemDecoration(context, orientation)
        .setDeco(drawable)
        .apply { showAfterLast = afterLast }
    addItemDecoration(decoration)
}

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

@BindingAdapter("onLoadMore")
fun RecyclerView.setLoadMoreListener(listener: OnBottomReachedListener) {
    clearOnScrollListeners()
    val scrollListener = EndlessRecyclerScrollListener(
        layoutManager ?: return,

        listener::onLoadMore
    )
    addOnScrollListener(scrollListener)
}

interface OnBottomReachedListener {
    fun onLoadMore()
}