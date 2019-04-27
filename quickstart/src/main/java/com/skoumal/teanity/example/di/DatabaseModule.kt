package com.skoumal.teanity.example.di

import android.content.Context
import androidx.room.Room
import com.skoumal.teanity.example.data.database.AppDatabase
import org.koin.dsl.module

val databaseModule = module {
    single { createDatabase(get()) }
}

fun createDatabase(context: Context): AppDatabase =
    Room.databaseBuilder(context, AppDatabase::class.java, AppDatabase.NAME).build()
