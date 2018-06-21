package com.skoumal.teanity.di

import android.arch.persistence.room.Room
import android.content.Context
import com.skoumal.teanity.data.database.AppDatabase
import org.koin.dsl.module.applicationContext

val databaseModule = applicationContext {
    bean { createDatabase(get()) }
    bean { createDbEntityDao(get()) }
}

fun createDatabase(context: Context): AppDatabase =
    Room.databaseBuilder(context, AppDatabase::class.java, AppDatabase.NAME).build()

fun createDbEntityDao(db: AppDatabase) =
    db.dbEntityDao()