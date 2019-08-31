package com.skoumal.teanity.network

import kotlinx.coroutines.Deferred
import retrofit2.Response
import java.io.IOException

fun <T : Any> Response<T>.toResult(): Result<T?> {
    if (isSuccessful) {
        return Result.success(body())
    }

    return Result.failure(IOException("Api error ${code()} ${message()}"))
}

suspend fun <T : Any> Deferred<Response<T>>.awaitResult(): Result<T?> {
    return try {
        await().toResult()
    } catch (e: Exception) {
        Result.failure(e)
    }
}
