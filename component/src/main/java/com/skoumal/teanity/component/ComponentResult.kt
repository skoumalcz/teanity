package com.skoumal.teanity.component

sealed class ComponentResult<out T> {

    val isFailure get() = this is Failure
    val isSuccess get() = this !is Failure

    fun getOrNull(): T? = when (this) {
        is Failure -> null
        is Success -> value
    }

    fun exceptionOrNull(): Throwable? = when (this) {
        is Failure -> throwable
        is Success -> null
    }

    private class Failure<T>(
        @JvmField
        val throwable: Throwable
    ) : ComponentResult<T>()

    private class Success<T>(
        val value: T
    ) : ComponentResult<T>()

    companion object {

        fun <T> success(value: T): ComponentResult<T> = Success(value)
        fun <T> failure(throwable: Throwable): ComponentResult<T> = Failure(throwable)

    }

}

@Suppress("NOTHING_TO_INLINE", "UNCHECKED_CAST")
inline fun <T> ComponentResult<T>.asPlatform() = when {
    isFailure -> Result.failure(exceptionOrNull()!!)
    else -> Result.success(getOrNull() as T)
}

inline fun <I, O> I.catching(body: I.() -> O) = try {
    ComponentResult.success(body())
} catch (e: Throwable) {
    ComponentResult.failure(e)
}

inline fun <T> ComponentResult<T>.getOrElse(body: Mapper<Throwable, T>) =
    asPlatform().getOrElse(body)

@Suppress("NOTHING_TO_INLINE")
inline fun <T> ComponentResult<T>.getOrDefault(value: T) =
    getOrElse { value }

inline fun <T> ComponentResult<T>.onSuccess(body: Mapper<T, Unit>) =
    asPlatform().onSuccess(body)

inline fun <T> ComponentResult<T>.onFailure(body: Mapper<Throwable, Unit>) =
    asPlatform().onFailure(body)

@Suppress("NOTHING_TO_INLINE")
inline fun <T> ComponentResult<T>.getOrThrow() =
    asPlatform().getOrThrow()

inline fun <T, R> ComponentResult<T>.map(mapper: Mapper<T, R>) =
    asPlatform().map(mapper)

inline fun <T, R> ComponentResult<T>.mapCatching(mapper: Mapper<T, R>) =
    asPlatform().mapCatching(mapper)

inline fun <T, R> ComponentResult<T>.flatMap(mapper: Mapper<T, ComponentResult<R>>) =
    mapCatching { mapper(it).getOrThrow() }

inline fun <T, R> ComponentResult<T>.fold(
    onSuccess: (T) -> R,
    onFailure: (Throwable) -> R,
) = asPlatform().fold(onSuccess, onFailure)

inline fun <T> ComponentResult<T>.recover(mapper: Mapper<Throwable, T>) =
    asPlatform().recover(mapper)

typealias Mapper<In, Out> = (In) -> Out