package com.skoumal.teanity.example.di

import com.skoumal.teanity.example.ui.MainViewModel
import com.skoumal.teanity.example.ui.home.HomeViewModel
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val viewModelModules = module {
    viewModel { MainViewModel() }
    viewModel { HomeViewModel() }
}