package com.skoumal.teanity.databinding

import androidx.databinding.BindingAdapter
import android.view.View
import com.bumptech.glide.request.RequestOptions

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