package com.skoumal.teanity.component.extensions

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.google.common.truth.Truth.assertThat
import com.skoumal.teanity.component.UseCase
import com.skoumal.teanity.component.invoke
import com.skoumal.teanity.tools.annotation.SubjectsToFutureChange
import kotlinx.coroutines.Dispatchers
import org.junit.Rule
import org.junit.Test

class UseCaseTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Test
    fun testUCExtension_isLoading() {
        val useCase = createUseCase {
            Thread.sleep(10)
        }
        val loading = useCase.isLoading().expectDistinct()

        assertThat(loading.value).isFalse()
        useCase()
        assertThat(loading.value).isFalse()
    }

    @Test
    fun testUCExtension_isIdle() {
        val useCase = createUseCase {
            Thread.sleep(10)
        }
        val idling = useCase.isIdle().expectDistinct()

        assertThat(idling.value).isTrue()
        useCase()
        assertThat(idling.value).isTrue()
    }

    @Test
    fun testUCExtension_isFailed() {
        val useCase = createUseCase {
            Thread.sleep(10)
        }
        val failed = useCase.isFailed().expectDistinct()

        assertThat(failed.value).isFalse()
        useCase()
        assertThat(failed.value).isFalse()
        useCase(null)
        assertThat(failed.value).isTrue()
    }

    // ---

    private fun LiveData<Boolean>.expectDistinct() = apply {
        var expectFalse: Boolean? = null
        observeForever {
            assertThat(expectFalse).isNotEqualTo(it)
            expectFalse = it
        }
    }

    @OptIn(SubjectsToFutureChange::class)
    private fun createUseCase(
        executor: (Unit) -> Unit
    ) = object : UseCase<Unit?, Unit>() {
        override val dispatcher = Dispatchers.Unconfined
        override suspend fun execute(input: Unit?) {
            executor(input!!)
        }
    }

}