package com.skoumal.teanity.util

import android.content.res.Resources
import androidx.annotation.StringRes

sealed class Text {

    abstract fun getText(resources: Resources): CharSequence

    class Resource(@StringRes private val res: Int, private vararg val args: Any) : Text() {
        override fun getText(resources: Resources): CharSequence {
            return resources.getString(res, *args)
        }
    }

    data class Sequence(private val charSequence: CharSequence) : Text() {
        override fun getText(resources: Resources): CharSequence {
            return charSequence
        }
    }
}