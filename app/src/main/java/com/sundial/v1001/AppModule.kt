package com.sundial.v1001

import com.sundial.v1001.service.CityService
import com.sundial.v1001.service.ICityService
import com.sundial.v1001.service.ITwilightService
import com.sundial.v1001.service.TwilightService
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { ApplicationViewModel(androidApplication()) }
    single <ICityService>{CityService() }
}