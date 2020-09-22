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

@Suppress("NOTHING_TO_INLINE")
inline fun <T> ComponentResult<T>.asPlatform() = when {
    isFailure -> Result.failure(exceptionOrNull()!!)
    else -> Result.success(getOrNull() as T)
}

inline fun <I, O> I.catching(body: I.() -> O) = try {
    ComponentResult.success(body())
} catch (e: Throwable) {
    ComponentResult.failure(e)
}