package com.skoumal.teanity.databinding

import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.skoumal.teanity.extensions.toPx
import com.skoumal.teanity.util.KItemDecoration

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

@BindingAdapter("dividerColor", "dividerHorizontal", "dividerSize", requireAll = false)
fun setDivider(view: RecyclerView, dividerColor: Int, dividerHorizontal: Boolean, _dividerSize: Int?) {

    val orientation = if (dividerHorizontal) RecyclerView.HORIZONTAL else RecyclerView.VERTICAL
    val dividerSize = _dividerSize ?: 1.toPx()
    val (width, height) = if (dividerHorizontal) dividerSize to 1 else 1 to dividerSize

    val drawable = GradientDrawable().apply {
        setSize(width, height)
        shape = GradientDrawable.RECTANGLE
        setColor(dividerColor)
    }

    val decoration = KItemDecoration(view.context, orientation).setDeco(drawable)
    view.addItemDecoration(decoration)
}
