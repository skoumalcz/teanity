package com.skoumal.teanity.ui.shape

import android.graphics.Path
import android.graphics.RectF
import androidx.annotation.UiThread

abstract class ShapeBuilder {

    protected var width: Float = 0f
        private set
    protected var height: Float = 0f
        private set

    fun setMeasuredSize(width: Int, height: Int) = apply {
        this.width = width.toFloat()
        this.height = height.toFloat()
    }

    /**
     * Builds a new path on UI thread. Argument [path] is always [Path.isEmpty]=true and always the
     * same instance as long as the view lives.
     *
     * By default it creates a path of a rectangle with [width] and [height] provided to the
     * builder.
     *
     * After the path has been successfully created, the view will take it in account according to
     * its documentation.
     * */
    @UiThread
    open fun build(path: Path) {
        path.addRect(
            RectF(0f, 0f, width, height),
            Path.Direction.CW
        )
    }

    companion object {

        internal operator fun invoke() = object : ShapeBuilder() {}

    }

}