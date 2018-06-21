package com.skoumal.teanity.model.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "entity")
data class DbEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val something: String
)