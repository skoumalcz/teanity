package com.skoumal.teanity.model.entity

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Urls(
        val raw: String,
        val full: String,
        val regular: String,
        val small: String,
        val thumb: String
)