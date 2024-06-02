package com.example.footapp.di

import com.example.footapp.DAO.BillDao
import com.example.footapp.repository.CustomerRepository
import com.example.footapp.ui.Order.offline.OfflineRepository
import dagger.Module
import dagger.Provides
import dagger.Reusable
import javax.inject.Singleton

@Module
object RepositoryModule {
    private var customerRepository: CustomerRepository? = null

    @Provides
    @Reusable
    @JvmStatic
    fun getCustomerRepository(): CustomerRepository {
        if (customerRepository == null) {
            customerRepository = CustomerRepository()
        }
        return customerRepository!!
    }

    @Provides
    @JvmStatic
    @Reusable
    fun provideOfflineRepository(billDao: BillDao): OfflineRepository {
        return OfflineRepository(billDao)
    }
}
