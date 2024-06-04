package com.example.footapp.di

import android.app.Application
import androidx.work.Configuration
import com.example.footapp.MyAppGlideModule
import com.example.footapp.UploadWorkerFactory
import javax.inject.Inject

class App : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: UploadWorkerFactory

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerApplicationComponent.builder().appModule(AppModule(this)).networkModule(NetworkModule).databaseModule(DatabaseModule)
            .repositoryComponent(RepositoryModule).build()
        appComponent.inject(this)
        MyAppGlideModule()
    }

    companion object {
        lateinit var appComponent: ApplicationComponent
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }
}
