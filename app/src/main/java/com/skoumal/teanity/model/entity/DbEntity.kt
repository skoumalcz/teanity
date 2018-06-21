package com.skoumal.teanity.model.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.skoumal.teanity.model.base.ComparableEntity

@Entity(tableName = "entity")
data class DbEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val something: String
) : ComparableEntity<DbEntity> {
    override fun sameAs(other: DbEntity): Boolean = id == other.id

    override fun contentSameAs(other: DbEntity): Boolean = something == other.something
}