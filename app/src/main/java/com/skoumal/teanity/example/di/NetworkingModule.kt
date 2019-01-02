package com.skoumal.teanity.example.di

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.skoumal.teanity.example.Constants
import com.skoumal.teanity.example.data.network.ApiServices
import com.skoumal.teanity.example.data.network.TokenInterceptor
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module.module
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val networkingModule = module {
    single { TokenInterceptor() }

    single { createOkHttpClient(get()) }

    single { createConverterFactory() }
    single { createCallAdapterFactory() }

    single { createRetrofit(get(), get(), get()) }

    single { createApiService<ApiServices>(get(), Constants.API_URL) }
}

fun createOkHttpClient(tokenInterceptor: TokenInterceptor): OkHttpClient {

    val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    return OkHttpClient.Builder()
        .addInterceptor(tokenInterceptor)
        .addInterceptor(httpLoggingInterceptor)
        .build()
}

fun createConverterFactory(): Converter.Factory {
    val moshi = Moshi.Builder().build()
    return MoshiConverterFactory.create(moshi)
}

fun createCallAdapterFactory(): CallAdapter.Factory {
    return CoroutineCallAdapterFactory()
}

fun createRetrofit(
    okHttpClient: OkHttpClient,
    converterFactory: Converter.Factory,
    callAdapterFactory: CallAdapter.Factory
): Retrofit.Builder {
    return Retrofit.Builder()
        .addConverterFactory(converterFactory)
        .addCallAdapterFactory(callAdapterFactory)
        .client(okHttpClient)
}

inline fun <reified T> createApiService(retrofitBuilder: Retrofit.Builder, baseUrl: String): T {
    return retrofitBuilder
        .baseUrl(baseUrl)
        .build()
        .create(T::class.java)
}