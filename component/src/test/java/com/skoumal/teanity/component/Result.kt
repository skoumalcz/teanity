package com.skoumal.teanity.component

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.Test

class ResultTest {

    @Test
    fun testCatchingResult() {
        val res: Result<String> = runBlocking {
            kotlin.runCatching {
                throw RuntimeException()
            }
        }

        assertThat(res.isFailure).isTrue()
    }

}