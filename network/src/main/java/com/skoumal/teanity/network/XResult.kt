package com.skoumal.teanity.network

inline fun <T, R> Result<List<T>>.mapList(mapper: (T) -> R) = when {
    isSuccess -> (getOrNull()?.map(mapper) ?: listOf()).let { Result.success(it) }
    else -> Result.failure(exceptionOrNull() ?: IllegalStateException("No exception was provided"))
}

inline fun <T, R> Result<List<T>>.flatMap(mapper: (T) -> List<R>) = when {
    isSuccess -> (getOrNull()?.map(mapper) ?: listOf()).let { Result.success(it.flatten()) }
    else -> Result.failure(exceptionOrNull() ?: IllegalStateException("No exception was provided"))
}
