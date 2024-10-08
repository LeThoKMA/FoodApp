package com.example.footapp.di

import android.app.Application
import android.content.Context
import com.example.footapp.ui.Order.HomeRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val mApplication: Application) {

    @Provides
    @Singleton
    @ApplicationContext
    fun provideContext(): Context {
        return mApplication
    }

}
