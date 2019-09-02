package com.skoumal.teanity.di.module

import com.skoumal.teanity.rxbus.RxBus
import org.koin.dsl.module

val teanityModule = module {
    single { RxBus() }
}