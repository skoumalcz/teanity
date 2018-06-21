package com.skoumal.teanity.data.repository

import com.skoumal.teanity.data.network.ApiServices
import com.skoumal.teanity.model.entity.Photo
import io.reactivex.Single


class PhotoRepository(
    private val api: ApiServices
) {

    fun getPhotos(page: Int = 1, perPage: Int = 10): Single<List<Photo>> = api.getPhotos(page, perPage)

}