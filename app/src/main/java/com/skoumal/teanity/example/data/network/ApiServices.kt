package com.skoumal.teanity.example.data.network

import com.skoumal.teanity.example.model.entity.inbound.Photo
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiServices {

    /**
     * This is just example how to use TokenInterceptor.NO_AUTH_HEADER.
     * Login is not working in this example.
     */
    @POST("login/")
    @Headers(TokenInterceptor.NO_AUTH_HEADER)
    fun login(): Deferred<Response<Unit>>

    @GET("photos/")
    fun getPhotos(
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 10
    ): Deferred<Response<List<Photo>>>
}
