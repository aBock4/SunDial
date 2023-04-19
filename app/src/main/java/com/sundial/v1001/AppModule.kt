package com.sundial.v1001

import com.sundial.v1001.service.CityService
import com.sundial.v1001.service.ICityService
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single {MainViewModel(get())}
    single<ICityService> { CityService(androidApplication()) }
    viewModel { ApplicationViewModel(androidApplication())}
}