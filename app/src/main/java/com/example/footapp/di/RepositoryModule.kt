package com.example.footapp.di

import com.example.footapp.DAO.BannerDao
import com.example.footapp.DAO.BillDao
import com.example.footapp.DAO.ItemDao
import com.example.footapp.DAO.QrDefaultDao
import com.example.footapp.DAO.TypeDBDao
import com.example.footapp.network.ApiService
import com.example.footapp.repository.CustomerRepository
import com.example.footapp.ui.Order.HomeRepository
import com.example.footapp.ui.Order.offline.OfflineRepository
import dagger.Module
import dagger.Provides
import dagger.Reusable
import javax.inject.Singleton

@Module
object RepositoryModule {
    private var customerRepository: CustomerRepository? = null

    @Provides
    @JvmStatic
    fun getCustomerRepository(
        bannerDao: BannerDao,
        qrDefaultDao: QrDefaultDao
    ): CustomerRepository {
        if (customerRepository == null) {
            customerRepository = CustomerRepository(bannerDao, qrDefaultDao)
        }
        return customerRepository!!
    }

    @Provides
    @JvmStatic
    @Reusable
    fun provideOfflineRepository(
        billDao: BillDao,
        typeDBDao: TypeDBDao,
        itemDao: ItemDao,
    ): OfflineRepository {
        return OfflineRepository(billDao, typeDBDao, itemDao)
    }

    @Provides
    @JvmStatic
    @Reusable
    fun provideHomeRepository(
        itemDao: ItemDao,
        typeDBDao: TypeDBDao,
        billDao: BillDao,
        bannerDao: BannerDao,
        qrDefaultDao: QrDefaultDao,
        apiService: ApiService
    ): HomeRepository {
        return HomeRepository(itemDao, typeDBDao, billDao, bannerDao, qrDefaultDao, apiService)
    }
}
