package com.skoumal.teanity.data.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.skoumal.teanity.model.entity.DbEntity

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