package com.example.footapp.di

import com.example.footapp.repository.CustomerRepository
import dagger.Module
import dagger.Provides
import dagger.Reusable

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
}
