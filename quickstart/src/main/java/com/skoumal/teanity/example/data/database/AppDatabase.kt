package com.skoumal.teanity.example.data.database

import androidx.room.RoomDatabase

/*@Database(
    version = 1,
    entities = []
)*/
/*FIXME uncomment this if you wish to use database*/
abstract class AppDatabase : RoomDatabase() {

    companion object {
        const val NAME = "database"
    }

}