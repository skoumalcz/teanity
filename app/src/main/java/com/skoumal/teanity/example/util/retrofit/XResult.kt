package com.skoumal.teanity.example.util.retrofit

import com.skoumal.teanity.api.Result
import kotlinx.coroutines.Deferred
import retrofit2.Response
import java.io.IOException

fun <T : Any> Response<T>.toResult(): Result<T> {
    if (isSuccessful) {
        val body = body()
        return if (body != null) {
            Result.Success(body)
        } else {
            Result.Void
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
