package com.demoapp.fyndapp

import android.app.Application
import com.demoapp.fyndapp.data.AppContainer

class TMDBApp : Application() {
    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(this)
    }

    companion object {
        lateinit var appContainer: AppContainer
    }
}