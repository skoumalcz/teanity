package com.skoumal.teanity.example.model.entity.internal

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.skoumal.teanity.util.ComparableEntity

@Entity(tableName = "entity")
data class DbEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val something: String
) : ComparableEntity<DbEntity> {
    override fun sameAs(other: DbEntity): Boolean = id == other.id

    override fun contentSameAs(other: DbEntity): Boolean = something == other.something
}