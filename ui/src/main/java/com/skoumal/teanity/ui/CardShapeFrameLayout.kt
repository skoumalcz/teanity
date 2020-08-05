package com.skoumal.teanity.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Path
import android.graphics.RectF
import android.os.Build
import android.util.AttributeSet
import android.util.LayoutDirection
import androidx.annotation.RequiresApi
import androidx.core.content.res.use
import com.skoumal.teanity.tools.annotation.RemoveOnDeprecation
import com.skoumal.teanity.ui.shape.ShapeBuilder

@Deprecated(
    message = "CornerLayout has been rewritten to conform with flexible base. In order to keep the naming scheme it has been renamed to CardShapeFrameLayout.",
    level = DeprecationLevel.WARNING,
    replaceWith = ReplaceWith("CardShapeFrameLayout")
)
@RemoveOnDeprecation("1.3")
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
open class CornerLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    style: Int = 0
) : CardShapeFrameLayout(context, attrs, style)

@Suppress("MemberVisibilityCanBePrivate")
@SuppressLint("Recycle")
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
open class CardShapeFrameLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    style: Int = 0
) : AbstractShapeFrameLayout(context, attrs, style) {

    private val shape = Shape(layoutDirection)

    var cornerRadius
        get() = shape.cornerRadius
        set(value) {
            shape.setCornerRadius(value)
            postInvalidate()
        }

    var corners
        get() = shape.corners
        set(value) {
            shape.setCorners(value)
            postInvalidate()
        }

    override fun onCreatePath(): ShapeBuilder = shape
        .setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
        .setClipToPadding(clipToPadding)
        .setMeasuredSize(width, height)

    init {
        context.obtainStyledAttributes(attrs, R.styleable.CardShapeFrameLayout, 0, 0).use {
            cornerRadius =
                it.getDimension(R.styleable.CardShapeFrameLayout_cornerRadius, cornerRadius)
            corners =
                it.getInt(R.styleable.CardShapeFrameLayout_corners, corners)
            clipToPadding =
                it.getBoolean(R.styleable.CardShapeFrameLayout_android_clipToPadding, clipToPadding)
        }
    }

    private class Shape(
        private val layoutDirection: Int
    ) : ShapeBuilder() {

        private val container: RectF
            get() = RectF(
                left.clipping * 1f,
                top.clipping * 1f,
                width - right.clipping * 1f,
                height - bottom.clipping * 1f
            )

        private var left = 0
        private var top = 0
        private var right = 0
        private var bottom = 0
        private var clipToPadding = false

        internal var corners = CORNER_TOP_END or CORNER_TOP_START
            private set
        internal var cornerRadius = 0f
            private set

        fun setClipToPadding(clipToPadding: Boolean) = apply {
            this.clipToPadding = clipToPadding
        }

        fun setPadding(
            left: Int = this.left,
            top: Int = this.top,
            right: Int = this.right,
            bottom: Int = this.bottom
        ) = apply {
            this.left = left
            this.top = top
            this.right = right
            this.bottom = bottom
        }

        fun setCorners(corners: Int) = apply {
            this.corners = corners
        }

        fun setCornerRadius(cornerRadius: Float) = apply {
            this.cornerRadius = cornerRadius
        }

        override fun build(path: Path) {
            val corners = getCornerRadii(cornerRadius)
            path.addRoundRect(container, corners, Path.Direction.CW)
        }

        private fun getCornerRadii(radius: Float): FloatArray {

            fun getCornerWhen(condition: Boolean) =
                if (condition) floatArrayOf(radius, radius)
                else floatArrayOf(0f, 0f)

            val topLeft = getCornerWhen(corners and CORNER_TOP_START != 0)
            val topRight = getCornerWhen(corners and CORNER_TOP_END != 0)
            val bottomRight = getCornerWhen(corners and CORNER_BOTTOM_END != 0)
            val bottomLeft = getCornerWhen(corners and CORNER_BOTTOM_START != 0)

            return if (layoutDirection == LayoutDirection.LTR) {
                topLeft + topRight + bottomRight + bottomLeft
            } else {
                topRight + topLeft + bottomLeft + bottomRight
            }
        }

        private val Int.clipping get() = if (clipToPadding) this else 0

    }

    companion object {
        const val CORNER_TOP_END = 1
        const val CORNER_TOP_START = 2
        const val CORNER_BOTTOM_END = 4
        const val CORNER_BOTTOM_START = 8
    }

}