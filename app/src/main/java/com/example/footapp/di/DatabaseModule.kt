package com.example.footapp.di

import android.content.Context
import androidx.room.Room
import com.example.footapp.DAO.AppDatabase
import com.example.footapp.DAO.BannerDao
import com.example.footapp.DAO.BillDao
import com.example.footapp.DAO.ItemDao
import com.example.footapp.DAO.TypeDBDao
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

    @Provides
    @Reusable
    fun provideItemDao(appDatabase: AppDatabase): ItemDao = appDatabase.itemDao()

    @Provides
    @Reusable
    fun provideTypeDao(appDatabase: AppDatabase): TypeDBDao = appDatabase.typeDao()

    @Provides
    @Reusable
    fun provideBannerDao(appDatabase: AppDatabase): BannerDao = appDatabase.bannerDao()
}
