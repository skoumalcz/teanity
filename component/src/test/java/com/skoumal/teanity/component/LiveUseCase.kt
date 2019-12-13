package com.skoumal.teanity.component

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class LiveUseCaseTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Test
    fun testInvoke() {
        val useCase = createUseCase<Int>()
        val input = 10
        val result = useCase(input)
        assertThat(result.value).isEqualTo(input)
    }

    @Test
    fun testRepeatedInvoke() {
        val useCase = createUseCase<Int>()
        val input = 10
        assertThat(useCase(input)).isNotEqualTo(useCase(input))
    }

    @Test
    fun testExtension() {
        val cls = LiveUseCase::class.java as Class<LiveUseCase<Unit, Unit>>
        val useCase = Mockito.mock<LiveUseCase<Unit, Unit>>(cls)
        // uses singular value before mocking as reference
        val result = useCase.invoke(Unit)

        Mockito.`when`(useCase.invoke(Unit)).thenReturn(result)

        assertThat(useCase.invoke()).isEqualTo(result)
    }

    // ---

    private fun <Input> createUseCase() = object : LiveUseCase<Input, Input>() {
        override fun execute(input: Input): LiveData<Input> {
            return MutableLiveData(input)
        }
    }

}