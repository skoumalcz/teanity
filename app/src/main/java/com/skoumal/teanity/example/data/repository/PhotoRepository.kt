package com.skoumal.teanity.example.data.repository


import com.skoumal.teanity.api.ApiXBuilder
import com.skoumal.teanity.api.IApiX
import com.skoumal.teanity.api.build
import com.skoumal.teanity.example.data.network.ApiServices


class PhotoRepository(
    private val api: ApiServices
) {

    fun getPhotos(helper: GetPhotos.() -> Unit = {}) = GetPhotos(helper)
        .build()
        .afterEvaluateSingle { api.getPhotos(page, limit) }

    class GetPhotos : ApiXBuilder<GetPhotos>(), IApiX {
        override var offset: Int = IApiX.OFFSET_START
        override var limit: Int = IApiX.GENERIC_LIMIT

        companion object : ApiXBuilder.Creator<GetPhotos>(GetPhotos::class)
    }

}