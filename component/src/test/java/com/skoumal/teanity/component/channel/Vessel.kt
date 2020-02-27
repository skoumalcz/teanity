package com.skoumal.teanity.component.channel

import com.google.common.truth.Truth.assertThat
import com.skoumal.teanity.tools.annotation.SubjectsToChange
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

@UseExperimental(SubjectsToChange::class, FlowPreview::class)
class VesselTest {

    private lateinit var vessel: Vessel<Vessel.Sailor>

    @Before
    fun prepare() {
        vessel = Vessel()
    }

    @Test
    fun testReception() {
        val sailor = object : Vessel.Sailor {}
        vessel.sail(sailor)

        runBlocking {
            val collection = mutableListOf<Vessel.Sailor>()

            vessel.dock()
                .onEach { collection.add(it) }
                .also { vessel.sink() }
                .collect()

            assertThat(collection).isNotEmpty()
            assertThat(collection[0]).isEqualTo(sailor)
        }
    }

    @Test
    fun testRepeatedReturn() {
        // kinda dumb, but tests whether the same channel is returned regardless of flow return type
        vessel.dock()
        vessel.dock()
    }

}