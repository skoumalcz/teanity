package com.skoumal.teanity.ui.binding

import android.os.Build
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.loadAny
import coil.transform.BlurTransformation
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation

@BindingAdapter("image", "crossfade", requireAll = false)
fun ImageView.loadCoilImage(url: Any?, crossfade: Boolean?) {
    loadAny(url) {
        crossfade(crossfade ?: true)
    }
}

@BindingAdapter("imageRounded", "imageCornerRadius", "crossfade", requireAll = false)
fun ImageView.loadCoilImageRounded(url: Any?, cornerRadius: Float, crossfade: Boolean?) {
    loadAny(url) {
        crossfade(crossfade ?: true)
        transformations(
            RoundedCornersTransformation(
                topLeft = cornerRadius,
                topRight = cornerRadius,
                bottomLeft = cornerRadius,
                bottomRight = cornerRadius
            )
        )
    }
}

@BindingAdapter("imageCircle", "crossfade", requireAll = false)
fun ImageView.loadCoilImageCircle(url: Any?, crossfade: Boolean?) {
    loadAny(url) {
        crossfade(crossfade ?: true)
        transformations(CircleCropTransformation())
    }
}

@BindingAdapter("imageBlur", "crossfade", requireAll = false)
fun ImageView.loadCoilImageBlur(url: Any?, crossfade: Boolean?) {
    loadAny(url) {
        crossfade(crossfade ?: true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            transformations(BlurTransformation(context))
        }
    }
}

@BindingAdapter(
    "imageBlur",
    "imageBlurRadius",
    "imageBlurSampling",
    "crossfade",
    requireAll = false
)
fun ImageView.loadCoilImageBlur(url: Any?, radius: Float, sampling: Float, crossfade: Boolean?) {
    loadAny(url) {
        crossfade(crossfade ?: true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            transformations(BlurTransformation(context, radius, sampling))
        }
    }
}