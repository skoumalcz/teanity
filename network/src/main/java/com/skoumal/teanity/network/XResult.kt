package com.skoumal.teanity.network

inline fun <T, R> Result<List<T>>.mapList(mapper: (T) -> R) = when {
    isSuccess -> (getOrNull()?.map(mapper) ?: listOf()).let { Result.success(it) }
    else -> this as Result<List<R>> // We don't give a f*. It's an exception anyways.
}

inline fun <T, R> Result<List<T>>.flatMap(mapper: (T) -> List<R>) = when {
    isSuccess -> (getOrNull()?.map(mapper) ?: listOf()).let { Result.success(it.flatten()) }
    else -> this as Result<List<R>> // We don't give a f*. It's an exception anyways.
}
