package com.skoumal.teanity.model.entity

import com.skoumal.teanity.model.base.ComparableEntity
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
    val id: String,
    val username: String,
    val name: String
) : ComparableEntity<User> {
    override fun sameAs(other: User): Boolean = id == other.id

    override fun contentSameAs(other: User): Boolean = username == other.username &&
            name == other.name
}