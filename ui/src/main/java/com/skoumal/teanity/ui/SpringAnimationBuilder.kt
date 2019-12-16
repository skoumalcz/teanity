package com.skoumal.teanity.ui

import android.view.View
import androidx.annotation.VisibleForTesting
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce

class SpringAnimationBuilder(private val view: View) {

    @VisibleForTesting
    internal val animations = mutableMapOf<DynamicAnimation.ViewProperty, SpringAnimation>()

    fun alpha(position: Float, force: AnimationBuilder? = null) =
        by(DynamicAnimation.ALPHA, position, force)

    fun translationX(position: Float, force: AnimationBuilder? = null) =
        by(DynamicAnimation.TRANSLATION_X, position, force)

    fun translationY(position: Float, force: AnimationBuilder? = null) =
        by(DynamicAnimation.TRANSLATION_Y, position, force)

    fun translationZ(position: Float, force: AnimationBuilder? = null) =
        by(DynamicAnimation.TRANSLATION_Z, position, force)

    fun scaleX(scale: Float, force: AnimationBuilder? = null) =
        by(DynamicAnimation.SCALE_X, scale, force)

    fun scaleY(scale: Float, force: AnimationBuilder? = null) =
        by(DynamicAnimation.SCALE_Y, scale, force)

    fun rotation(rotation: Float, force: AnimationBuilder? = null) =
        by(DynamicAnimation.ROTATION, rotation, force)

    fun rotationX(rotation: Float, force: AnimationBuilder? = null) =
        by(DynamicAnimation.ROTATION_X, rotation, force)

    fun rotationY(rotation: Float, force: AnimationBuilder? = null) =
        by(DynamicAnimation.ROTATION_Y, rotation, force)

    fun x(position: Float, force: AnimationBuilder? = null) =
        by(DynamicAnimation.X, position, force)

    fun y(position: Float, force: AnimationBuilder? = null) =
        by(DynamicAnimation.Y, position, force)

    fun scrollX(scroll: Float, force: AnimationBuilder? = null) =
        by(DynamicAnimation.SCROLL_X, scroll, force)

    fun scrollY(scroll: Float, force: AnimationBuilder? = null) =
        by(DynamicAnimation.SCROLL_Y, scroll, force)

    fun start() = apply {
        animations.forEach { it.value.start() }
    }

    private fun by(
        property: DynamicAnimation.ViewProperty,
        finalPosition: Float,
        force: AnimationBuilder?
    ) = SingleAnimationBuilder(finalPosition)
        .apply { force?.invoke(this) }
        .let { by(property, it) }

    private fun by(
        property: DynamicAnimation.ViewProperty,
        animation: SingleAnimationBuilder
    ) = apply {
        animations[property] = SpringAnimation(view, property)
            .apply(animation.animation)
            .apply { setStartValue(property.getValue(view)) }
            .setSpring(animation.force)
    }

    inner class SingleAnimationBuilder(finalPosition: Float) {

        internal val force = SpringForce(finalPosition)
        internal var animation: SpringBuilder = {}

        fun force(builder: SpringForceBuilder) {
            force.apply(builder)
        }

        fun animation(builder: SpringBuilder) {
            animation = builder
        }
    }

}

typealias AnimationBuilder = SpringAnimationBuilder.SingleAnimationBuilder.() -> Unit
typealias SpringForceBuilder = SpringForce.() -> Unit
typealias SpringBuilder = SpringAnimation.() -> Unit