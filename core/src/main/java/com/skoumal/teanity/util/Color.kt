package com.skoumal.teanity.util

import android.content.Context
import android.content.res.ColorStateList
import androidx.annotation.ColorRes
import com.skoumal.teanity.extensions.colorCompat
import com.skoumal.teanity.extensions.colorStateListCompat
import android.graphics.Color as AColor

sealed class Color {
    abstract fun getColor(context: Context): ColorStateList

    data class StateList(@ColorRes private val res: Int) : Color() {
        override fun getColor(context: Context): ColorStateList {
            return context.colorStateListCompat(res) ?: ColorStateList.valueOf(AColor.BLACK)
        }
    }

    data class ColorInt(private val color: Int) : Color() {
        override fun getColor(context: Context): ColorStateList {
            return ColorStateList.valueOf(color)
        }
    }

    data class Resource(@ColorRes private val res: Int) : Color() {
        override fun getColor(context: Context): ColorStateList {
            return ColorStateList.valueOf(context.colorCompat(res) ?: AColor.BLACK)
        }
    }

    companion object {
        val alert = ColorInt(0xF44336)
        val success = ColorInt(0x4CAF50)
    }
}