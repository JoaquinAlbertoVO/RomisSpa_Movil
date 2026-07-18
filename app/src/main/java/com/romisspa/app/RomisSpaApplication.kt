package com.romisspa.app

import android.app.Application
import com.romisspa.app.di.AppContainer

class RomisSpaApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
