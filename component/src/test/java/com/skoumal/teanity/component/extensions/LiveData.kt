package com.skoumal.teanity.component.extensions

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import kotlin.random.Random.Default.nextInt

class LiveDataTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Test
    fun testLiveData_map() {
        val input = nextInt()
        val liveData = MutableLiveData(input)
        val result = liveData.map { "$it" }

        result.observeForever {}

        assertThat(result.value).isEqualTo("$input")
    }

    @Test
    fun testLiveData_flatMap() {
        val input = nextInt()
        val liveData = MutableLiveData(input)
        val result = liveData.flatMap { MutableLiveData("$it") }

        result.observeForever {}

        assertThat(result.value).isEqualTo("$input")
    }

    @Test
    fun testLiveData_distinctUntilChanged() {
        val input = nextInt()
        val liveData = MutableLiveData(input)
        val result = liveData.distinctUntilChanged()

        var lastValue: Int? = null
        result.observeForever {
            assertThat(it).isNotEqualTo(lastValue)
            lastValue = it
        }

        liveData.postValue(input)
        liveData.postValue(input)
    }

}