package com.sundial.v1001

import android.app.Application
import org.koin.android.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext
import org.koin.core.logger.Level

/**
 * MySundialApplication is the main class for the Sundial app.  Responsible for Koin dependency
 * injection framework and providing the application context.
 */
class MySundialApplication : Application(){

    /**
     * Called when the application is starting up and initializes Koin injection
     */
    override fun onCreate(){
        super.onCreate()

        GlobalContext.startKoin{
            androidLogger(if(BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@MySundialApplication)
            modules(appModule)
        }
    }
}