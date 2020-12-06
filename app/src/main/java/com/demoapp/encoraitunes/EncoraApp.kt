package com.demoapp.encoraitunes

import android.app.Application
import com.demoapp.encoraitunes.data.AppContainer

class EncoraApp : Application() {
    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(this)
    }

    companion object {
        lateinit var appContainer: AppContainer
    }
}