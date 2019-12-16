package com.skoumal.teanity.ui

import android.view.View
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import com.google.common.truth.Truth.assertThat
import com.skoumal.teanity.ui.extensions.springify
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import kotlin.random.Random.Default.nextFloat

class SpringAnimationBuilderTest {

    @Mock
    lateinit var view: View
    lateinit var builder: SpringAnimationBuilder

    @Before
    fun prepare() {
        view = Mockito.mock(View::class.java)
        builder = view.springify()
    }


    @Test
    fun testAnimation_force() {
        val inputPosition = nextFloat()
        val inputStiffness = listOf(
            SpringForce.STIFFNESS_HIGH,
            SpringForce.STIFFNESS_MEDIUM,
            SpringForce.STIFFNESS_LOW,
            SpringForce.STIFFNESS_VERY_LOW
        ).random()
        val inputDampingRatio = listOf(
            SpringForce.DAMPING_RATIO_HIGH_BOUNCY,
            SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY,
            SpringForce.DAMPING_RATIO_LOW_BOUNCY,
            SpringForce.DAMPING_RATIO_NO_BOUNCY
        ).random()

        builder.alpha(inputPosition) {
            force {
                dampingRatio = inputDampingRatio
                stiffness = inputStiffness
                assertThat(finalPosition).isEqualTo(inputPosition)
            }
        }

        val animation = builder.animations[DynamicAnimation.ALPHA]
        assertThat(animation).isNotNull()
        assertThat(animation!!.spring.dampingRatio).isEqualTo(inputDampingRatio)
        assertThat(animation.spring.stiffness).isEqualTo(inputStiffness)
    }

    @Test
    fun testAnimation_alpha() {
        val input = nextFloat()
        builder.alpha(input)
        builder.animations[DynamicAnimation.ALPHA].requireFinalPosition(input)
    }

    @Test
    fun testAnimation_translationX() {
        val input = nextFloat()
        builder.translationX(input)
        builder.animations[DynamicAnimation.TRANSLATION_X].requireFinalPosition(input)
    }

    @Test
    fun testAnimation_translationY() {
        val input = nextFloat()
        builder.translationY(input)
        builder.animations[DynamicAnimation.TRANSLATION_Y].requireFinalPosition(input)
    }

    @Test
    fun testAnimation_translationZ() {
        val input = nextFloat()
        builder.translationZ(input)
        builder.animations[DynamicAnimation.TRANSLATION_Z].requireFinalPosition(input)
    }

    @Test
    fun testAnimation_scaleX() {
        val input = nextFloat()
        builder.scaleX(input)
        builder.animations[DynamicAnimation.SCALE_X].requireFinalPosition(input)
    }

    @Test
    fun testAnimation_scaleY() {
        val input = nextFloat()
        builder.scaleY(input)
        builder.animations[DynamicAnimation.SCALE_Y].requireFinalPosition(input)
    }

    @Test
    fun testAnimation_rotation() {
        val input = nextFloat()
        builder.rotation(input)
        builder.animations[DynamicAnimation.ROTATION].requireFinalPosition(input)
    }

    @Test
    fun testAnimation_rotationX() {
        val input = nextFloat()
        builder.rotationX(input)
        builder.animations[DynamicAnimation.ROTATION_X].requireFinalPosition(input)
    }

    @Test
    fun testAnimation_rotationY() {
        val input = nextFloat()
        builder.rotationY(input)
        builder.animations[DynamicAnimation.ROTATION_Y].requireFinalPosition(input)
    }

    @Test
    fun testAnimation_x() {
        val input = nextFloat()
        builder.x(input)
        builder.animations[DynamicAnimation.X].requireFinalPosition(input)
    }

    @Test
    fun testAnimation_y() {
        val input = nextFloat()
        builder.y(input)
        builder.animations[DynamicAnimation.Y].requireFinalPosition(input)
    }

    @Test
    fun testAnimation_scrollX() {
        val input = nextFloat()
        builder.scrollX(input)
        builder.animations[DynamicAnimation.SCROLL_X].requireFinalPosition(input)
    }

    @Test
    fun testAnimation_scrollY() {
        val input = nextFloat()
        builder.scrollY(input)
        builder.animations[DynamicAnimation.SCROLL_Y].requireFinalPosition(input)
    }

    @Test
    fun testAnimation_start() {
        assertThat(builder.animations).isEmpty()
        builder.start()
    }


    private fun SpringAnimation?.requireFinalPosition(finalPosition: Float) {
        assertThat(this).isNotNull()
        assertThat(this!!.spring.finalPosition).isEqualTo(finalPosition)
    }

}