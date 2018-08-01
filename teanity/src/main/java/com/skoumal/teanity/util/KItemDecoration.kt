package com.skoumal.teanity.util

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.skoumal.teanity.extensions.drawableCompat

class KItemDecoration(private val context: Context, @RecyclerView.Orientation orientation: Int) :
    DividerItemDecoration(context, orientation) {

    fun setDeco(@DrawableRes drawable: Int) = apply {
        setDeco(context.drawableCompat(drawable))
    }

    fun setDeco(drawable: Drawable?) = apply {
        drawable?.let { setDrawable(it) }
    }

}