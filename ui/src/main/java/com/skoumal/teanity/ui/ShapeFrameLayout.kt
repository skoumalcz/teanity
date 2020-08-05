package com.skoumal.teanity.ui

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import androidx.annotation.RequiresApi
import com.skoumal.teanity.ui.shape.ShapeBuilder

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class ShapeFrameLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    style: Int = 0
) : AbstractShapeFrameLayout(context, attrs, style) {

    var shape: ShapeBuilder? = null
        set(value) {
            field = value
            postInvalidate()
        }

    override fun onCreatePath(): ShapeBuilder {
        val shape = shape ?: ShapeBuilder.invoke()
        return shape.apply {
            setMeasuredSize(measuredWidth, measuredHeight)
        }
    }

}