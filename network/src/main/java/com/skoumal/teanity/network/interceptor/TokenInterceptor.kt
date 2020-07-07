package com.skoumal.teanity.network.interceptor

import androidx.annotation.Keep
import com.skoumal.teanity.network.interceptor.TokenInterceptor.Companion.NO_AUTH
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import retrofit2.http.Headers
import java.io.IOException

/**
 * Adds a authentication to existing headers.
 *
 * ### Caveats
 *
 * #### Static declaration
 *
 * As this instance will be static, it will provide the same auth details forever. If you're using
 * some form of DI, make sure you're using factory declarations (creates the instance every time on
 * invocation), otherwise the value is kept the same forever.
 *
 * Otherwise you can override the [TokenInterceptor], and consequently have [headerValue] open for
 * your custom getter from shared preferences (or elsewhere).
 *
 * #### Don't need it everywhere
 *
 * You can annotate the given service method with @[Headers] and add [NO_AUTH] to bypass
 * authentication for this endpoint.
 * */
@Keep
open class TokenInterceptor(
    private val headerName: String,
    protected open val headerValue: String
) : Interceptor {

    constructor(scheme: Scheme, value: String) : this(
        scheme.field,
        scheme.scheme.format(value)
    )

    companion object {
        private const val NO_AUTH_HEADER_FIELD = "No-authentication"
        const val NO_AUTH = "$NO_AUTH_HEADER_FIELD: true"
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().edit {
            when {
                it.header(NO_AUTH_HEADER_FIELD) == null -> addHeader(headerName, headerValue)
                else -> removeHeader(NO_AUTH_HEADER_FIELD)
            }
        }

        return chain.proceed(request)
    }

    protected fun Request.edit(builder: Request.Builder.(Request) -> Unit): Request = newBuilder()
        .apply { builder(this@edit) }
        .build()

    enum class Scheme(val field: String, val scheme: String) {
        Basic("Authorization", "Basic %s"),
        Bearer("Authorization", "Bearer %s"),
        Digest("Authorization", "Digest %s"),
    }

}