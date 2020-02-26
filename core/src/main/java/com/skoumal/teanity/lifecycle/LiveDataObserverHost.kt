package com.skoumal.teanity.lifecycle

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

typealias LiveDataObserver<T> = (T) -> Unit

interface LiveDataObserverHost {

    fun <T> LiveData<T>.observe(observer: Observer<T>): Observer<T>
    fun <T> LiveData<T>.observe(listener: LiveDataObserver<T>): Observer<T>
    fun clearObservers()

    companion object {
        val impl: LiveDataObserverHost get() = LiveDataObserverHostImpl()
    }

}

private class LiveDataObserverHostImpl : LiveDataObserverHost {

    @Volatile
    private var observers: HashSet<ObserverHolder<*>>? = null

    override fun <T> LiveData<T>.observe(observer: Observer<T>) = observer.also {
        observeForever(it)
        provideRegistry().add(ObserverHolder(this, observer))
    }

    override fun <T> LiveData<T>.observe(listener: LiveDataObserver<T>) =
        observe(Observer { t -> listener(t) })

    @Synchronized
    override fun clearObservers() {
        with(provideRegistry()) {
            forEach { it.clear() }
            clear()
        }
    }

    @Synchronized
    private fun provideRegistry(): HashSet<ObserverHolder<*>> {
        if (observers == null) {
            observers = hashSetOf()
        }
        return observers!!
    }

    private class ObserverHolder<T>(
        private val observable: LiveData<T>,
        private val observer: Observer<T>
    ) {
        fun clear() = observable.removeObserver(observer)
    }
}
