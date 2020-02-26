package com.skoumal.teanity.observable

import androidx.databinding.Bindable
import androidx.databinding.Observable
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import kotlin.random.Random.Default.nextBoolean
import kotlin.random.Random.Default.nextBytes

class NotifyableTest {

    private lateinit var notifyable: NotifyableTestImpl
    private var defaultValue: Boolean = false

    @Before
    fun prepare() {
        defaultValue = nextBoolean()
        notifyable = NotifyableTestImpl()
    }

    @Test
    fun testDefaultValue() {
        with(notifyable) {
            assertThat(testField).isEqualTo(defaultValue)
        }
    }

    @Test
    fun testUpdate() {
        with(notifyable) {
            testField = false
            assertThat(testField).isFalse()
            testField = true
            assertThat(testField).isTrue()
        }
    }

    @Test
    fun testMapped() {
        with(notifyable) {
            testField = false
            assertThat(mappedField).isEqualTo(falseValue)
            testField = true
            assertThat(mappedField).isEqualTo(trueValue)
        }
    }

    @Test
    fun testListener() {
        with(notifyable) {
            var ranListener = false
            val listener = provideListener {
                ranListener = true
                assertThat(it).isAnyOf(BR_testField, BR_mappedField)
            }
            addOnPropertyChangedCallback(listener)

            testField = true

            assertThat(ranListener).isTrue()

            removeOnPropertyChangedCallback(listener)
            ranListener = false

            testField = false

            assertThat(ranListener).isFalse()
        }
    }

    @Test
    fun testAllListener() {
        with(notifyable) {
            var ranListener = false
            val listener = provideListener {
                ranListener = true
                assertThat(it).isEqualTo(0)
            }
            addOnPropertyChangedCallback(listener)

            testFieldNotifyAll = !defaultValue

            assertThat(ranListener).isTrue()
        }
    }

    private inline fun provideListener(
        crossinline callback: (Int) -> Unit
    ) = object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            callback(propertyId)
        }
    }

    inner class NotifyableTestImpl : Notifyable by Notifyable.impl {

        val trueValue = String(nextBytes(10))
        val falseValue = String(nextBytes(10))

        var testFieldNotifyAll by observable(defaultValue)
        var testField by observable(defaultValue, BR_testField)
            @Bindable get

        val mappedField: String
            @Bindable get() {
                return when (testField) {
                    true -> trueValue
                    else -> falseValue
                }
            }
    }

    companion object {
        private val BR_testField = 1
        private val BR_mappedField = 2
    }

}