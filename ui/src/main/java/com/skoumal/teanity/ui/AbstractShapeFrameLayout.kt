package com.skoumal.teanity.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Outline
import android.graphics.Path
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import com.skoumal.teanity.tools.log.error
import com.skoumal.teanity.tools.log.info
import com.skoumal.teanity.ui.shape.ShapeBuilder

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
abstract class AbstractShapeFrameLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    style: Int = 0
) : FrameLayout(context, attrs, style) {

    protected val clipPath = Path()
        get() {
            if (field.isEmpty) {
                onCreatePath().build(field)
                if (field.isEmpty) {
                    error("has fetched empty view path")
                }
            }
            return field
        }

    private val isInitialized = true

    @Suppress("ConstantConditionIf")
    override fun setClipToPadding(clipToPadding: Boolean) {
        if (isInitialized) { // this is not constant, since the clip to padding is invoked when the view inits
            clipPath.reset()
        }
        super.setClipToPadding(clipToPadding)
    }

    abstract fun onCreatePath(): ShapeBuilder

    init {
        outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View?, outline: Outline?) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    outline?.setPath(clipPath)
                } else {
                    @Suppress("DEPRECATION")
                    runCatching { outline?.setConvexPath(clipPath) }
                        .onFailure { info("Cannot set non-convex path to outline") }
                }
            }
        }
        clipToOutline = true
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
        postInvalidate()
    }

    @Suppress("ConstantConditionIf")
    override fun invalidate() {
        if (isInitialized) {
            clipPath.reset()
        }
        super.invalidate()
    }

}