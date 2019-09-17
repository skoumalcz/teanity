package com.skoumal.teanity.ui

import android.widget.ImageView
import com.bumptech.glide.request.RequestOptions

@Deprecated("")
object Transformations {
    const val NONE = 0
    const val CENTER_CROP = 1
    const val CENTER_INSIDE = 2
    const val FIT_CENTER = 3
    const val CIRCLE_CROP = 4
}

@Deprecated("Use transformation by ImageView.ScaleType, ScaleType or String")
@Suppress("DEPRECATION")
fun RequestOptions.applyTransformation(transform: Int): RequestOptions = when (transform) {
    Transformations.NONE -> this
    Transformations.CENTER_CROP -> centerCrop()
    Transformations.CENTER_INSIDE -> centerInside()
    Transformations.FIT_CENTER -> fitCenter()
    Transformations.CIRCLE_CROP -> circleCrop()
    else -> throw IllegalArgumentException("Unsupported transformation")
}

enum class ScaleType(val text: String) {
    CIRCLE_CROP("circleCrop"),
    CENTER_CROP("centerCrop"),
    CENTER_INSIDE("centerInside"),
    FIT_CENTER("fitCenter"),

    @Deprecated("Not supported.")
    FIT_END("fitEnd"),
    @Deprecated("Not supported.")
    FIT_START("fitStart"),
    @Deprecated("Not supported.")
    FIT_XY("fitXY"),
    @Deprecated("Not supported.")
    MATRIX("matrix"),
    @Deprecated("Not supported.")
    CENTER("center")
}

fun RequestOptions.applyTransformation(scaleType: ImageView.ScaleType) =
    applyTransformation(scaleType.asInternal())

fun RequestOptions.applyTransformation(scaleType: ScaleType) =
    applyTransformation(scaleType.text)

@Suppress("DEPRECATION")
fun RequestOptions.applyTransformation(scaleType: String): RequestOptions = when (scaleType) {
    ScaleType.CIRCLE_CROP.text -> circleCrop()
    ScaleType.CENTER_CROP.text -> centerCrop()
    ScaleType.CENTER_INSIDE.text -> centerInside()
    ScaleType.FIT_CENTER.text -> fitCenter()

    ScaleType.FIT_END.text, ScaleType.FIT_START.text, ScaleType.FIT_XY.text, ScaleType.MATRIX.text,
    ScaleType.CENTER.text -> throw IllegalArgumentException("Scale type \"$scaleType\" is not supported.")

    else -> throw IllegalArgumentException("Scale type \"$scaleType\" is not supported.")
}

private fun ImageView.ScaleType.asInternal() = when (this) {
    ImageView.ScaleType.MATRIX -> ScaleType.MATRIX
    ImageView.ScaleType.FIT_XY -> ScaleType.FIT_XY
    ImageView.ScaleType.FIT_START -> ScaleType.FIT_START
    ImageView.ScaleType.FIT_CENTER -> ScaleType.FIT_CENTER
    ImageView.ScaleType.FIT_END -> ScaleType.FIT_END
    ImageView.ScaleType.CENTER -> ScaleType.CENTER
    ImageView.ScaleType.CENTER_CROP -> ScaleType.CENTER_CROP
    ImageView.ScaleType.CENTER_INSIDE -> ScaleType.CENTER_INSIDE
}