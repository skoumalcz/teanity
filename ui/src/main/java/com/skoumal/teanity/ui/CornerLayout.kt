package com.skoumal.teanity.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Outline
import android.graphics.Path
import android.graphics.RectF
import android.os.Build
import android.util.AttributeSet
import android.util.LayoutDirection
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.FrameLayout
import androidx.core.content.res.use

@Suppress("MemberVisibilityCanBePrivate")
@SuppressLint("Recycle")
open class CornerLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    style: Int = 0
) : FrameLayout(context, attrs, style) {

    protected open val clipPath = Path()
        get() {
            if (field.isEmpty) {
                val rectf = if (includePadding) RectF(
                    paddingLeft * 1f,
                    paddingTop * 1f,
                    width - paddingRight * 1f,
                    height - paddingBottom * 1f
                ) else RectF(0f, 0f, width * 1f, height * 1f)
                val corners = getCornerRadii(cornerRadius)
                field.addRoundRect(rectf, corners, Path.Direction.CW)
            }
            return field
        }

    var includePadding = true
        set(value) {
            field = value
            clipPath.reset()
            invalidate()
        }

    var cornerRadius = 0f
        set(value) {
            field = value
            clipPath.reset()
            invalidate()
        }

    var corners = CORNER_TOP_END or CORNER_TOP_START
        set(value) {
            field = value
            clipPath.reset()
            invalidate()
        }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (!isInEditMode) {
                outlineProvider = object : ViewOutlineProvider() {
                    override fun getOutline(view: View?, outline: Outline?) {
                        outline?.setConvexPath(clipPath)
                    }
                }
            }
            clipToOutline = true
        }
        context.obtainStyledAttributes(attrs, R.styleable.CornerLayout, 0, 0).use {
            cornerRadius = it.getDimension(R.styleable.CornerLayout_cornerRadius, cornerRadius)
            corners = it.getInt(R.styleable.CornerLayout_corners, corners)
            includePadding = it.getBoolean(R.styleable.CornerLayout_includePadding, includePadding)
        }
    }

    private fun getCornerRadii(radius: Float): FloatArray {

        fun getCornerWhen(condition: Boolean) =
            if (condition) floatArrayOf(radius, radius)
            else floatArrayOf(0f, 0f)

        val topLeft = getCornerWhen(corners and CORNER_TOP_START != 0)
        val topRight = getCornerWhen(corners and CORNER_TOP_END != 0)
        val bottomRight = getCornerWhen(corners and CORNER_BOTTOM_END != 0)
        val bottomLeft = getCornerWhen(corners and CORNER_BOTTOM_START != 0)

        return if (
            Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1 ||
            layoutDirection == LayoutDirection.LTR
        ) {
            topLeft + topRight + bottomRight + bottomLeft
        } else {
            topRight + topLeft + bottomLeft + bottomRight
        }
    }

    /** Clips drawing of this view and backing root implementation */
    override fun draw(canvas: Canvas?) {
        canvas?.clipPath(clipPath)
        super.draw(canvas)
    }

    /** Clips child views */
    override fun dispatchDraw(canvas: Canvas?) {
        canvas?.clipPath(clipPath)
        super.dispatchDraw(canvas)
    }

    /** Clips view's drawing */
    override fun onDraw(canvas: Canvas?) {
        canvas?.clipPath(clipPath)
        super.onDraw(canvas)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        clipPath.reset()
    }

    companion object {
        const val CORNER_TOP_END = 1
        const val CORNER_TOP_START = 2
        const val CORNER_BOTTOM_END = 4
        const val CORNER_BOTTOM_START = 8
    }

}