package com.example.footapp.di

import android.app.Application
import com.example.footapp.MyAppGlideModule

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerApplicationComponent.builder().appModule(AppModule(this)).build()
        appComponent.inject(this)
        MyAppGlideModule()
    }

    companion object {
        lateinit var appComponent: ApplicationComponent
    }
}
