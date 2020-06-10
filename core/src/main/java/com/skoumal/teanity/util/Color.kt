package com.skoumal.teanity.util

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import android.graphics.Color as AColor

sealed class Color {
    abstract fun getColor(context: Context): ColorStateList

    data class StateList(@ColorRes private val res: Int) : Color() {
        override fun getColor(context: Context): ColorStateList {
            return colorStateListCompat(context) ?: ColorStateList.valueOf(AColor.BLACK)
        }

        private fun colorStateListCompat(context: Context) = try {
            AppCompatResources.getColorStateList(context, res)
        } catch (e: Resources.NotFoundException) {
            null
        }
    }

    data class ColorInt(private val color: Int) : Color() {
        override fun getColor(context: Context): ColorStateList {
            return ColorStateList.valueOf(color)
        }
    }

    data class Resource(@ColorRes private val res: Int) : Color() {
        override fun getColor(context: Context): ColorStateList {
            return ColorStateList.valueOf(colorCompat(context) ?: AColor.BLACK)
        }

        private fun colorCompat(context: Context) = try {
            ContextCompat.getColor(context, res)
        } catch (e: Resources.NotFoundException) {
            null
        }
    }

    data class Attribute(@AttrRes private val res: Int) : Color() {
        override fun getColor(context: Context): ColorStateList {
            return with(context.theme.obtainStyledAttributes(intArrayOf(res))) {
                getColorStateList(0) ?: ColorStateList.valueOf(getColor(0, AColor.BLACK))
            }
        }
    }

    companion object {
        val alert = ColorInt(0xF44336)
        val success = ColorInt(0x4CAF50)
    }
}