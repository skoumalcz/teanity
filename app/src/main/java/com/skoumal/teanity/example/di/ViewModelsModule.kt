package com.skoumal.teanity.example.di

import com.skoumal.teanity.example.ui.MainViewModel
import com.skoumal.teanity.example.ui.home.HomeViewModel
import com.skoumal.teanity.example.ui.login.LoginEmailViewModel
import com.skoumal.teanity.example.ui.login.LoginViewModel
import com.skoumal.teanity.example.ui.photodetail.PhotoDetailViewModel
import com.skoumal.teanity.example.ui.settings.SettingsViewModel
import org.koin.android.architecture.ext.viewModel
import org.koin.dsl.module.applicationContext

val viewModelModules = applicationContext {
    viewModel { LoginEmailViewModel(get(), get()) }
    viewModel { MainViewModel() }
    viewModel { HomeViewModel(get()) }
    viewModel { SettingsViewModel(get()) }
    viewModel { PhotoDetailViewModel() }
    viewModel { LoginViewModel() }
}