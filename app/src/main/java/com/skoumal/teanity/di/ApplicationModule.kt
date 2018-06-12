package com.skoumal.teanity.di

import android.content.Context
import com.skoumal.teanity.model.Model
import com.skoumal.teanity.model.ModelImpl
import com.skoumal.teanity.model.rxbus.RxBus
import org.koin.dsl.module.applicationContext

val applicationModule = applicationContext {
    bean { RxBus() }
    bean { ModelImpl(get()) as Model }
    bean { get<Context>().resources }
}