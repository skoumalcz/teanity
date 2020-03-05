package com.skoumal.teanity.observable

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import timber.log.Timber

@UseExperimental(ExperimentalCoroutinesApi::class)
interface Broadcastable<E> {

    fun openSubscription(): ReceiveChannel<E>
    fun offer(element: E): Boolean
    fun disposeBroadcastChannel()
    fun E.publish()

    companion object {

        fun <E> impl(): Broadcastable<E> = BroadcastableImpl()

    }

}

@UseExperimental(ExperimentalCoroutinesApi::class)
private class BroadcastableImpl<E> : Broadcastable<E> {

    private val viewEvents = BroadcastChannel<E>(Channel.BUFFERED)

    override fun openSubscription(): ReceiveChannel<E> {
        return viewEvents.openSubscription()
    }

    override fun offer(element: E): Boolean {
        return viewEvents.offer(element)
    }

    override fun disposeBroadcastChannel() {
        viewEvents.close()
    }

    override fun E.publish() {
        if (viewEvents.isClosedForSend) {
            Timber.i("Channel has been disposed, no further events will be sent")
            return
        }
        if (!viewEvents.offer(this)) {
            Timber.i("Event $this has been rejected by broadcast queue")
        }
    }

}