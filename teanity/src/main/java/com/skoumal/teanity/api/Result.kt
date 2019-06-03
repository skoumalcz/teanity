package com.skoumal.teanity.api

sealed class Result<out T : Any> {

    data class Success<out T : Any>(val data: T) : Result<T>() {
        companion object {
            operator fun invoke() = Success(Unit)
        }
    }

    data class Error(val exception: Exception) : Result<Nothing>()

    val isSuccess get() = this is Success
    val isError get() = this is Error

    override fun toString(): String {
        return when (this) {
            is Success -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }
}

inline fun <T : Any, R : Any> Result<T>.map(block: (T) -> R): Result<R> {
    return when (this) {
        is Result.Error -> this
        is Result.Success -> Result.Success(block(data))
    }
}