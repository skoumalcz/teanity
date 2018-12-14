package com.skoumal.teanity.example.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.skoumal.teanity.example.model.entity.DbEntity

@Database(
    version = 1, //Increment this as you change your entities
    entities = [
        DbEntity::class
        //, AnotherEntity::class ...
    ]
)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        const val NAME = "database"
    }

    abstract fun dbEntityDao(): DbEntityDao
    //Define more DAOs

}