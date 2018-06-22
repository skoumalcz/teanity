package com.skoumal.teanity.example.data.repository


import com.skoumal.teanity.example.data.network.ApiServices
import com.skoumal.teanity.example.model.entity.Photo
import io.reactivex.Single


class PhotoRepository(
    private val api: ApiServices
) {

    fun getPhotos(page: Int = 1, perPage: Int = 10): Single<List<Photo>> = api.getPhotos(page, perPage)

}