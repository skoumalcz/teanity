package com.skoumal.teanity.data.network

import com.skoumal.teanity.Constants
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class TokenInterceptor : Interceptor {

    companion object {
        private const val NO_AUTH_HEADER_NAME = "No-authentication"
        const val NO_AUTH_HEADER = "$NO_AUTH_HEADER_NAME: true"
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        if (request.header(NO_AUTH_HEADER_NAME) == null) {
            val token = "Client-ID ${Constants.API_ACCESS_KEY}"
            request = request.newBuilder()
                .addHeader("Authorization", token)
                .build()
        } else {
            request = request.newBuilder()
                .removeHeader(NO_AUTH_HEADER_NAME)
                .build()
        }

        return chain.proceed(request)
    }
}