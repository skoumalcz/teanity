package com.skoumal.teanity.model.entity

import com.skoumal.teanity.model.base.ComparableEntity
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Photo(
    val id: String,
    val description: String?,
    val urls: Urls,
    val user: User
) : ComparableEntity<Photo> {
    override fun sameAs(other: Photo): Boolean = id == other.id

    override fun contentSameAs(other: Photo): Boolean = description == other.description &&
            urls == other.urls &&
            user == other.user
}