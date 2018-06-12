package com.skoumal.teanity.di

import com.skoumal.teanity.ui.MainViewModel
import com.skoumal.teanity.ui.home.HomeViewModel
import com.skoumal.teanity.ui.login.LoginEmailViewModel
import com.skoumal.teanity.ui.photodetail.PhotoDetailViewModel
import com.skoumal.teanity.ui.settings.SettingsViewModel
import org.koin.android.architecture.ext.viewModel
import org.koin.dsl.module.applicationContext

val viewModelModules = applicationContext {
    viewModel { LoginEmailViewModel(get(), get()) }
    viewModel { MainViewModel() }
    viewModel { HomeViewModel(get()) }
    viewModel { SettingsViewModel(get()) }
    viewModel { PhotoDetailViewModel() }
}