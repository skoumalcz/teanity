package com.skoumal.teanity.model.entity

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Photo(
    val id: String,
    val description: String?,
    val urls: Urls,
    val user: User
)