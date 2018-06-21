package com.skoumal.teanity.model.entity

import com.skoumal.teanity.model.base.ComparableEntity
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Urls(
    val raw: String,
    val full: String,
    val regular: String,
    val small: String,
    val thumb: String
) : ComparableEntity<Urls> {
    override fun sameAs(other: Urls): Boolean = contentSameAs(other)

    override fun contentSameAs(other: Urls): Boolean = raw == other.raw &&
            full == other.full &&
            regular == other.regular &&
            small == other.small &&
            thumb == other.thumb
}