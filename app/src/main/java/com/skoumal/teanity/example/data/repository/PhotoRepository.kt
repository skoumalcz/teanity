package com.skoumal.teanity.example.data.repository


import com.skoumal.teanity.api.ApiX
import com.skoumal.teanity.example.data.network.ApiServices
import com.skoumal.teanity.example.model.entity.Photo
import io.reactivex.Single


class PhotoRepository(
    private val api: ApiServices
) {

    fun getPhotos(apiExtender: ApiX.() -> Unit = {}): Single<List<Photo>> {
        val requestDefinition = ApiX.Default().apply(apiExtender)
        return api.getPhotos(
            page = requestDefinition.page,
            perPage = requestDefinition.limit
        )
    }

}