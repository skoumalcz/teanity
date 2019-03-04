package com.skoumal.teanity.example.data.repository

import com.skoumal.teanity.api.ApiX
import com.skoumal.teanity.api.Result
import com.skoumal.teanity.example.data.network.ApiServices
import com.skoumal.teanity.example.model.entity.Photo
import com.skoumal.teanity.example.model.entity.awaitResult

class PhotoRepository(
    private val api: ApiServices
) {

    suspend fun getPhotos(apiExtender: ApiX.() -> Unit = {}): Result<List<Photo>> {
        val requestDefinition = ApiX.Default().apply(apiExtender)
        return api.getPhotos(
            page = requestDefinition.page,
            perPage = requestDefinition.limit
        ).awaitResult()
    }

}