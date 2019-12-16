package com.skoumal.teanity.network

import com.skoumal.teanity.network.exception.NetworkException
import kotlinx.coroutines.Deferred
import retrofit2.Response
import timber.log.Timber

fun <T : Any> Response<T>.toResult(): Result<T?> {
    if (isSuccessful) {
        return Result.success(body())
    }

    Timber.e("Remote Error [code=${code()},message=${message()}]")
    return Result.failure(NetworkException(errorBody()))
}

@Deprecated("Retrofit no longer requires Deferred implementation and supports suspend by default")
suspend fun <T : Any> Deferred<Response<T>>.awaitResult(): Result<T?> {
    return try {
        await().toResult()
    } catch (e: Exception) {
        Result.failure(e)
    }
}
