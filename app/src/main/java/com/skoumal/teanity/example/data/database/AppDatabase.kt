package com.skoumal.teanity.example.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.skoumal.teanity.example.model.entity.DbEntity

@Database(
    version = 1,
    entities = [DbEntity::class]
)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        const val NAME = "database"
    }

    abstract fun dbEntityDao(): DbEntityDao
}