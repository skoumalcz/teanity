package com.skoumal.teanity.example.di

import android.content.Context
import com.skoumal.teanity.example.model.Model
import com.skoumal.teanity.example.model.ModelImpl
import com.skoumal.teanity.rxbus.RxBus
import org.koin.dsl.module.applicationContext

val applicationModule = applicationContext {
    bean { RxBus() }
    bean { ModelImpl(get()) as Model }
    bean { get<Context>().resources }
}