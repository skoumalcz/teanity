package com.skoumal.teanity.example.di

import androidx.room.Room
import android.content.Context
import com.skoumal.teanity.example.data.database.AppDatabase
import org.koin.dsl.module.module

val databaseModule = module {
    single { createDatabase(get()) }
    single { createDbEntityDao(get()) }
}

fun createDatabase(context: Context): AppDatabase =
    Room.databaseBuilder(context, AppDatabase::class.java, AppDatabase.NAME).build()

fun createDbEntityDao(db: AppDatabase) =
    db.dbEntityDao()