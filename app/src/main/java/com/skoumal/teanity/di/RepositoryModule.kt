package com.skoumal.teanity.di

import com.skoumal.teanity.data.repository.PhotoRepository
import org.koin.dsl.module.applicationContext

val repositoryModule = applicationContext {
    bean { PhotoRepository(get()) }
}