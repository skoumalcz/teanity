package com.skoumal.teanity.databinding

import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.InsetDrawable
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.skoumal.teanity.extensions.startEndToLeftRight
import com.skoumal.teanity.extensions.toPx
import com.skoumal.teanity.util.KItemDecoration
import kotlin.math.roundToInt

@BindingAdapter("gone")
fun setGone(view: View, gone: Boolean) {
    view.visibility = if (gone) View.GONE else View.VISIBLE
}

@BindingAdapter("invisible")
fun setInvisible(view: View, invisible: Boolean) {
    view.visibility = if (invisible) View.INVISIBLE else View.VISIBLE
}

object Transformations {
    const val NONE = 0
    const val CENTER_CROP = 1
    const val CENTER_INSIDE = 2
    const val FIT_CENTER = 3
    const val CIRCLE_CROP = 4
}

fun RequestOptions.applyTransformation(transformation: Int): RequestOptions = when (transformation) {
    Transformations.NONE -> this
    Transformations.CENTER_CROP -> centerCrop()
    Transformations.CENTER_INSIDE -> centerInside()
    Transformations.FIT_CENTER -> fitCenter()
    Transformations.CIRCLE_CROP -> circleCrop()
    else -> throw IllegalArgumentException("Unsupported transformation")
}

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
fun setDivider(
    view: RecyclerView,
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
    val (marginLeft, marginRight) = view.context.startEndToLeftRight(marginStart, marginEnd)

    val drawable = GradientDrawable().apply {
        setSize(width, height)
        shape = GradientDrawable.RECTANGLE
        setColor(color)
    }.let {
        InsetDrawable(it, marginLeft, marginTop, marginRight, marginBottom)
    }

    val decoration = KItemDecoration(view.context, orientation)
        .setDeco(drawable)
        .apply { showAfterLast = afterLast }
    view.addItemDecoration(decoration)
}
