package com.skoumal.teanity.component

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import kotlin.random.Random.Default.nextBytes
import kotlin.random.Random.Default.nextInt

class CompoundUseCaseTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Test
    fun testInvoke_result() {
        val input = text
        val useCase = createTypedUseCase<String> { input }

        runBlocking {
            assertThat(useCase.invoke(Unit).getOrNull()).isEqualTo(input)
        }
    }

    @Test
    fun testInvoke_resultObserved() {
        var hasRun = false
        val input = text
        val useCase = createTypedUseCase<String> { input }

        useCase.observeData().observeForever {
            hasRun = true
            assertThat(it.isSuccess).isTrue()
            assertThat(it.getOrNull()).isEqualTo(input)
        }

        assertThat(hasRun).isFalse()

        runBlocking {
            assertThat(useCase.invoke(Unit).getOrNull()).isEqualTo(input)
            assertThat(hasRun).isTrue()
        }
    }

    @Test
    fun testInvoke_observe() {
        var hasRun = false
        val input = text
        val useCase = createTypedUseCase<String> { input }

        useCase.observe(Unit).observeForever {
            hasRun = true
            assertThat(it).isEqualTo(input)
        }

        assertThat(hasRun).isTrue()
    }

    @Test
    fun testInvoke_state() {
        var hasRun = false
        val input = text
        val useCase = createTypedUseCase<String> {
            hasRun = true
            assertThat(this.observeState().value).isEqualTo(UseCaseState.LOADING)
            input
        }

        runBlocking {
            useCase.invoke(Unit)
        }

        assertThat(hasRun).isTrue()
    }

    @Test
    fun testInvoke_stateException() {
        var hasRun = false
        val useCase = createTypedUseCase<String> {
            hasRun = true
            throw IllegalArgumentException()
        }

        runBlocking {
            assertThat(useCase.invoke(Unit).isFailure).isTrue()
        }

        assertThat(hasRun).isTrue()
    }

    @Test
    fun testInvoke_externalData() {
        var hasRun = false
        val input = text
        val useCase = createTypedUseCase<String> { input }
        val data = useCase.provide().also {
            it.observeForever {
                hasRun = true
            }
        }

        useCase.observeData().observeForever {
            throw IllegalStateException("This cannot be called!")
        }

        runBlocking {
            useCase.invoke(Unit, data)
        }

        assertThat(hasRun).isTrue()
    }

    // ---

    private val text = String(nextBytes(nextInt(10, 30)))

    private fun <Output> createTypedUseCase(
        executor: TestUseCase<Output>.(LiveData<UseCaseState>) -> Output
    ) = TestUseCase(executor)

    private fun createUseCase(
        executor: TestUseCase<Unit>.(LiveData<UseCaseState>) -> Unit
    ) = TestUseCase(executor)

    class TestUseCase<Output>(
        private val executor: TestUseCase<Output>.(LiveData<UseCaseState>) -> Output
    ) : CompoundUseCase<Unit, Output>() {
        val testState = observeState()
        override val dispatcher = Dispatchers.Unconfined
        override suspend fun execute(input: Unit): Output {
            return executor(testState)
        }
    }

}