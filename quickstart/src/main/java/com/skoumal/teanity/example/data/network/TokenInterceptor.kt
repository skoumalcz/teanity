package com.skoumal.teanity.example.data.network

import com.skoumal.teanity.example.Constants
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class TokenInterceptor : Interceptor {

    companion object {
        private const val NO_AUTH_HEADER_NAME = "No-authentication"
        private const val AUTH_HEADER_NAME = "Authorization"

        const val NO_AUTH_HEADER = "$NO_AUTH_HEADER_NAME: true"
    }

    private val token get() = "Client-ID ${Constants.API_ACCESS_KEY}"

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().edit {
            if (it.header(NO_AUTH_HEADER_NAME) == null) {
                addHeader(AUTH_HEADER_NAME, token)
            } else {
                removeHeader(NO_AUTH_HEADER_NAME)
            }
        }

        return chain.proceed(request)
    }

    private fun Request.edit(builder: Request.Builder.(Request) -> Unit): Request = newBuilder()
        .apply { builder(this@edit) }
        .build()
}