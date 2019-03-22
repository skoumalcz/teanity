package com.skoumal.teanity.example.di

import android.content.Context
import com.skoumal.teanity.rxbus.RxBus
import org.koin.dsl.module

val applicationModule = module {
    single { RxBus() }
    single { get<Context>().resources }
}