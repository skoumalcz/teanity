package com.skoumal.teanity.example.di

import android.content.Context
import com.skoumal.teanity.example.model.Model
import com.skoumal.teanity.example.model.ModelImpl
import com.skoumal.teanity.rxbus.RxBus
import org.koin.dsl.module.module

val applicationModule = module {
    single { RxBus() }
    single { ModelImpl(get()) as Model }
    single { get<Context>().resources }
}