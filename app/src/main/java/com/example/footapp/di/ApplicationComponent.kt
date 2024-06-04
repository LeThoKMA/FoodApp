package com.example.footapp.di

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerFactory
import com.example.footapp.ui.Order.HomeRepository
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, RepositoryModule::class, NetworkModule::class, DatabaseModule::class])
interface ApplicationComponent {
    fun inject(app: App)

    @ApplicationContext
    fun getContext(): Context

    @Component.Builder
    interface Builder {
        fun build(): ApplicationComponent

        fun appModule(appModule: AppModule): Builder
        fun networkModule(networkModule: NetworkModule): Builder
        fun repositoryComponent(repository: RepositoryModule): Builder
        fun databaseModule(databaseModule: DatabaseModule): Builder
    }
}
