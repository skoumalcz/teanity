package com.skoumal.teanity.api

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

sealed class Result<out T : Any> {

    data class Success<out T : Any>(val data: T) : Result<T>() {
        companion object {
            operator fun invoke() = Success(Unit)
        }
    }

    object Void : Result<Nothing>()

    data class Error(val exception: Exception) : Result<Nothing>()

    val isSuccess get() = this is Success
    val isError get() = this is Error

    override fun toString(): String {
        return when (this) {
            is Success -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            is Void -> "Void[no result]"
        }
    }
}

@UseExperimental(ExperimentalContracts::class)
inline fun <T : Any, R : Any> Result<T>.transform(block: (T) -> R): Result<R> {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }
    return when (this) {
        is Result.Error -> this
        is Result.Void -> this
        is Result.Success -> Result.Success(block(data))
    }
}

@UseExperimental(ExperimentalContracts::class)
inline fun <T : Any, R : Any> Result<T>.transformToResult(block: (T) -> Result<R>): Result<R> {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }
    return when (this) {
        is Result.Error -> this
        is Result.Void -> this
        is Result.Success -> block(data)
    }
}

inline fun <Data : Any, Stream : List<Data>, R : Any> Result<Stream>.map(block: (Data) -> R): Result<List<R>> {
    return when (this) {
        is Result.Error -> this
        is Result.Void -> this
        is Result.Success -> Result.Success(data.map(block))
    }
}