package com.example.footapp.di

import android.content.Context
import androidx.room.Room
import com.example.footapp.DAO.AppDatabase
import com.example.footapp.DAO.BillDao
import dagger.Module
import dagger.Provides
import dagger.Reusable
import javax.inject.Singleton

@Module
object DatabaseModule {
    @Provides
    @Reusable
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "bill-database",
        ).build()
    }

    @Provides
    @Reusable
    fun provideBillDao(appDatabase: AppDatabase): BillDao {
        return appDatabase.billDao()
    }
}
