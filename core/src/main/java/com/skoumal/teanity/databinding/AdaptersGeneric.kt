package com.skoumal.teanity.databinding

import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.databinding.BindingAdapter

@BindingAdapter("gone")
fun View.setGone(gone: Boolean) {
    isGone = gone
}

@BindingAdapter("invisible")
fun View.setInvisible(invisible: Boolean) {
    isInvisible = invisible
}

@BindingAdapter("goneUnless")
fun View.setGoneUnless(goneUnless: Boolean) {
    setGone(goneUnless.not())
}

@BindingAdapter("invisibleUnless")
fun View.setInvisibleUnless(invisibleUnless: Boolean) {
    setInvisible(invisibleUnless.not())
}
