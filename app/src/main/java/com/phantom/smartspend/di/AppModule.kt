package com.phantom.smartspend.di

import com.phantom.smartspend.data.repository.AuthRepository
import com.phantom.smartspend.viewmodels.AuthViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single { AuthRepository(androidContext()) }

    viewModel { AuthViewModel(get()) }
}