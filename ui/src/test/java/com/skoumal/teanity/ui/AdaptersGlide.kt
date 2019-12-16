package com.skoumal.teanity.ui

import android.widget.ImageView
import com.bumptech.glide.request.RequestOptions
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import kotlin.random.Random.Default.nextBytes

class AdaptersGlideTest {

    @Mock
    lateinit var options: RequestOptions
    @Mock
    lateinit var optionsCircleCrop: RequestOptions
    @Mock
    lateinit var optionsCenterCrop: RequestOptions
    @Mock
    lateinit var optionsCenterInside: RequestOptions
    @Mock
    lateinit var optionsFitCenter: RequestOptions

    @Before
    fun prepare() {
        options = Mockito.mock(RequestOptions::class.java)
        optionsCircleCrop = Mockito.mock(RequestOptions::class.java)
        optionsCenterCrop = Mockito.mock(RequestOptions::class.java)
        optionsCenterInside = Mockito.mock(RequestOptions::class.java)
        optionsFitCenter = Mockito.mock(RequestOptions::class.java)

        Mockito.`when`(options.circleCrop()).thenReturn(optionsCircleCrop)
        Mockito.`when`(options.centerCrop()).thenReturn(optionsCenterCrop)
        Mockito.`when`(options.centerInside()).thenReturn(optionsCenterInside)
        Mockito.`when`(options.fitCenter()).thenReturn(optionsFitCenter)
    }

    // region deprecated members

    @Test
    fun testTransformation_random() {
        try {
            val array = ByteArray(10)
            nextBytes(array)
            options.applyTransformation(String(array))
            throw RuntimeException()
        } catch (e: Exception) {
            assertThat(e).hasMessageThat().contains("is not supported")
        }
    }

    @Test
    fun testTransformation_fitEnd() {
        try {
            options.applyTransformation(ImageView.ScaleType.FIT_END)
            throw RuntimeException()
        } catch (e: Exception) {
            assertThat(e).hasMessageThat().contains("is not supported")
        }
    }

    @Test
    fun testTransformation_fitStart() {
        try {
            options.applyTransformation(ImageView.ScaleType.FIT_START)
            throw RuntimeException()
        } catch (e: Exception) {
            assertThat(e).hasMessageThat().contains("is not supported")
        }
    }

    @Test
    fun testTransformation_fitXY() {
        try {
            options.applyTransformation(ImageView.ScaleType.FIT_XY)
            throw RuntimeException()
        } catch (e: Exception) {
            assertThat(e).hasMessageThat().contains("is not supported")
        }
    }

    @Test
    fun testTransformation_matrix() {
        try {
            options.applyTransformation(ImageView.ScaleType.MATRIX)
            throw RuntimeException()
        } catch (e: Exception) {
            assertThat(e).hasMessageThat().contains("is not supported")
        }
    }

    @Test
    fun testTransformation_center() {
        try {
            options.applyTransformation(ImageView.ScaleType.CENTER)
            throw RuntimeException()
        } catch (e: Exception) {
            assertThat(e).hasMessageThat().contains("is not supported")
        }
    }

    // endregion

    @Test
    fun testTransformation_circleCrop() {
        val result = options.applyTransformation(ScaleType.CIRCLE_CROP)
        assertThat(result).isEqualTo(optionsCircleCrop)
    }

    @Test
    fun testTransformation_centerCrop() {
        val result = options.applyTransformation(ImageView.ScaleType.CENTER_CROP)
        assertThat(result).isEqualTo(optionsCenterCrop)
    }

    @Test
    fun testTransformation_centerInside() {
        val result = options.applyTransformation(ImageView.ScaleType.CENTER_INSIDE)
        assertThat(result).isEqualTo(optionsCenterInside)
    }

    @Test
    fun testTransformation_fitCenter() {
        val result = options.applyTransformation(ImageView.ScaleType.FIT_CENTER)
        assertThat(result).isEqualTo(optionsFitCenter)
    }

}