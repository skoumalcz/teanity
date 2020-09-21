package com.skoumal.teanity.component

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.common.truth.Truth.assertThat
import com.skoumal.teanity.tools.annotation.SubjectsToFutureChange
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import kotlin.random.Random.Default.nextInt

@OptIn(SubjectsToFutureChange::class)
class UseCaseTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Test
    fun testState() {
        val useCase = createUseCase {
            assertThat(it.value).isEqualTo(UseCaseState.LOADING)
        }

        useCase.testState.observeForever {}

        assertThat(useCase.testState.value).isEqualTo(UseCaseState.IDLE)
        runBlocking { useCase.now(Unit) }
        assertThat(useCase.testState.value).isEqualTo(UseCaseState.IDLE)
    }

    @Test
    fun testState_executionException() {
        val useCase = createUseCase {
            throw IllegalStateException()
        }

        val result = useCase.observe()
        useCase.testState.observeForever {}
        result.observeForever {}

        useCase()

        assertThat(useCase.testState.value).isEqualTo(UseCaseState.FAILED)
        val r = result.value
        if (r != null) {
            assertThat(r.isFailure).isTrue()
        } else {
            throw NullPointerException()
        }
    }

    @Test
    fun testInvoke_extensionResult() {
        val desiredResult = nextInt()
        val useCase = createTypedUseCase<Int> { desiredResult }
        val result = MutableLiveData<ComponentResult<Int>>()

        useCase(result)

        val r = result.value
        if (r != null) {
            assertThat(r.getOrNull()).isEqualTo(desiredResult)
        } else {
            throw NullPointerException()
        }
    }

    // ---

    private fun <Output> createTypedUseCase(
        executor: TestUseCase<Output>.(LiveData<UseCaseState>) -> Output
    ) = TestUseCase(executor)

    private fun createUseCase(
        executor: TestUseCase<Unit>.(LiveData<UseCaseState>) -> Unit
    ) = TestUseCase(executor)

    class TestUseCase<Output>(
        private val executor: TestUseCase<Output>.(LiveData<UseCaseState>) -> Output
    ) : UseCase<Unit, Output>() {
        val testState = observeState()
        override val dispatcher = Dispatchers.Unconfined
        override suspend fun execute(input: Unit): Output {
            return executor(testState)
        }
    }

}