package com.skoumal.teanity.example.model.entity

import com.skoumal.teanity.api.Result
import kotlinx.coroutines.Deferred
import retrofit2.Response
import java.io.IOException

fun <T : Any> Response<T>.toResult(): Result<T> {
    if (isSuccessful) {
        val body = body()
        if (body != null) {
            return Result.Success(body)
        }
    }

    return Result.Error(
        IOException("Api error ${code()} ${message()}")
    )
}

suspend fun <T : Any> Deferred<Response<T>>.awaitResult(): Result<T> {
    return try {
        await().toResult()
    } catch (e: Exception) {
        Result.Error(e)
    }
}
