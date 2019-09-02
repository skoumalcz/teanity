package com.skoumal.teanity.di

import android.content.Context
import com.skoumal.teanity.di.module.genericModule
import com.skoumal.teanity.di.module.teanityModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module

object Teanity {

    fun modules() = genericModule + teanityModule

    @JvmStatic
    fun startWith(_context: Context, vararg modules: Module) {
        val context = _context.applicationContext
        startKoin {
            androidContext(context)
            modules(modules.toList() + modules())
        }
    }

}