package com.skoumal.teanity.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView

class KItemDecoration(private val context: Context, @RecyclerView.Orientation orientation: Int) : DividerItemDecoration(context, orientation) {

    fun setDeco(@DrawableRes drawable: Int) = apply {
        setDeco(context.drawableCompat(drawable))
    }

    fun setDeco(drawable: Drawable?) = apply {
        drawable?.let { setDrawable(it) }
    }

}