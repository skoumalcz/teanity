package com.skoumal.teanity.di

import android.content.Context
import com.skoumal.teanity.di.module.genericModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module

object Teanity {

    @JvmStatic
    fun modules() = listOf(genericModule)

    @JvmStatic
    fun startWith(_context: Context, vararg modules: Module) {
        startWith(_context, modules.toList())
    }

    @JvmStatic
    fun startWith(_context: Context, modules: List<Module>) {
        val context = _context.applicationContext
        startKoin {
            androidContext(context)
            modules(modules + modules())
        }
    }

}