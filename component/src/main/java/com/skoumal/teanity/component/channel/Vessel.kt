package com.skoumal.teanity.component.channel

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow

/**
 * Successor to [com.skoumal.teanity.rxbus.RxBus].
 *
 * As opposed to RxBus, where you had one RxBus for all the things, you will create multiple vessels
 * for your sailors.
 *
 * You are also required to manage the "sinking" of your vessel. After you're done with sending data
 * through this vessel you can safely call [sink], the channel will be marked as closed for sending
 * and is automatically disposed of once all elements are received. See [Channel.close] for
 * reference.
 * */
@OptIn(ExperimentalCoroutinesApi::class)
class Vessel<Sailor : Vessel.Sailor> {

    @Volatile
    private lateinit var channel: Channel<Sailor>

    private fun getChannel(
        requireImplicitlyNew: Boolean = ::channel.isInitialized && channel.isClosedForSend
    ): Channel<Sailor> {
        if (!::channel.isInitialized || requireImplicitlyNew) {
            return Channel<Sailor>(Channel.UNLIMITED).also { channel = it }
        }
        return channel
    }

    fun sail(sailor: Sailor) = getChannel().offer(sailor)
    fun dock() = getChannel(false).consumeAsFlow()
    fun sink() = if (::channel.isInitialized) channel.close() else true

    interface Sailor

}