package com.skoumal.teanity.example.util

import androidx.databinding.BindingAdapter
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import androidx.core.view.isInvisible
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

@BindingAdapter("invisible")
fun setGroupInvisible(group: Group, invisible: Boolean) {
    group.isInvisible = invisible
    group.updatePreLayout(group.parent as ConstraintLayout)
}

@BindingAdapter("invisibleUnless")
fun setGroupInvisibleUnless(group: Group, invisibleUnless: Boolean) {
    setGroupInvisible(group, invisibleUnless.not())
}
