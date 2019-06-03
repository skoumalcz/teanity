package com.skoumal.teanity.example.data.repository

import com.skoumal.teanity.example.data.network.ApiServices
import com.skoumal.teanity.example.util.retrofit.awaitResult

class PhotoRepository(
    private val api: ApiServices
) {

    suspend fun getPhotos(offset: Int = 0, limit: Int = 10) = api.getPhotos(offset / limit, limit).awaitResult()

}