package com.skoumal.teanity.util

import android.databinding.BindingAdapter
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.request.RequestOptions
import com.skoumal.teanity.GlideApp

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

@BindingAdapter("url", "transformation", requireAll = false)
fun setImageFromUrl(view: ImageView, url: String?, transformation: Int) {
    if (url.isNullOrBlank()) {
        return
    }

    val options = RequestOptions().applyTransformation(transformation)

    GlideApp.with(view.context)
        .load(url)
        .apply(options)
        .into(view)
}

fun RequestOptions.applyTransformation(transformation: Int): RequestOptions = when (transformation) {
    Transformations.NONE -> this
    Transformations.CENTER_CROP -> centerCrop()
    Transformations.CENTER_INSIDE -> centerInside()
    Transformations.FIT_CENTER -> fitCenter()
    Transformations.CIRCLE_CROP -> circleCrop()
    else -> throw IllegalArgumentException("Unsupported transformation")
}