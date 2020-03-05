package com.skoumal.teanity.observable

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Test
import kotlin.random.Random.Default.nextBytes

class BroadcastableTest {

    private lateinit var broadcastable: Broadcastable<String>

    @Before
    fun prepare() {
        broadcastable = Broadcastable.impl()
    }

    @Test
    fun testReceive() {
        val testInput = getTestInput()
        var hasRun = false
        GlobalScope.launch(Dispatchers.Unconfined) {
            broadcastable.openSubscription().consumeAsFlow().collect {
                hasRun = true
                assertThat(it).isEqualTo(testInput)
            }
        }

        broadcastable.offer(testInput)

        assertThat(hasRun).isTrue()
    }

    @Test
    fun testDispose() {
        broadcastable.disposeBroadcastChannel()

        val result = kotlin.runCatching { testReceive() }

        assertThat(result.isFailure).isTrue()
    }

    @Test
    fun testClientDispose() {
        // this simulates the subscription that's being saved in the TeanityDelegate
        val sub = broadcastable.openSubscription()
        val testInput = getTestInput()
        var hasRun = false
        GlobalScope.launch(Dispatchers.Unconfined) {
            // the sub is launched and >everything< is being collected
            sub.consumeAsFlow().collect {
                hasRun = true
                assertThat(it).isEqualTo(testInput)
            }
        }

        // we post the item from viewModel
        broadcastable.offer(testInput)
        // we except this to pass
        assertThat(hasRun).isTrue()

        hasRun = false

        // we simulate the configuration change (aka destroy / dispose)
        sub.cancel()

        // and this should pass just fine since the subscription shouldn't be transparent
        assertThat(broadcastable.offer(testInput)).isTrue()
        // and the initial event collection shouldn't pass
        assertThat(hasRun).isFalse()
    }

    // ---

    private fun getTestInput() = String(nextBytes(30))

}