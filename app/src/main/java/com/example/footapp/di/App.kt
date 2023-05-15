package com.example.footapp.di

import android.app.Application

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerApplicationComponent.builder().appModule(AppModule(this)).build()
        appComponent.inject(this)
    }

    companion object {
        lateinit var appComponent: ApplicationComponent
    }
}
