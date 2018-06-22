package com.skoumal.teanity.example.util

import android.databinding.BindingAdapter
import android.widget.ImageView
import com.bumptech.glide.request.RequestOptions
import com.skoumal.teanity.databinding.applyTransformation
import com.skoumal.teanity.example.GlideApp

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