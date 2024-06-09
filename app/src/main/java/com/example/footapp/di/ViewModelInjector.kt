package com.example.footapp.di

import com.example.footapp.MainViewModel
import com.example.footapp.ui.Account.AccountViewModel
import com.example.footapp.ui.Order.OrderViewModel
import com.example.footapp.ui.Order.offline.OfflineConfirmViewModel
import com.example.footapp.ui.Order.offline.OrderWhenNetworkErrorViewModel
import com.example.footapp.ui.customer.CustomerViewModel
import com.example.footapp.ui.login.LoginViewModel
import com.example.footapp.ui.orderlist.OrderListViewModel
import com.example.footapp.ui.pay.PayConfirmViewModel
import com.example.footapp.ui.statistic.StatisticViewModel
import com.example.footapp.ui.user.AddUserViewModel
import dagger.Component

@Component(
    modules = [NetworkModule::class, RepositoryModule::class, DatabaseModule::class],
    dependencies = [ApplicationComponent::class],
)
@ViewModelScope
interface ViewModelInjector {
    fun inject(viewModel: LoginViewModel)
    fun inject(viewModel: OrderViewModel)
    fun inject(viewModel: CustomerViewModel)
    fun inject(viewModel: PayConfirmViewModel)
    fun inject(viewModel: OrderListViewModel)
    fun inject(viewModel: AccountViewModel)
    fun inject(viewModel: StatisticViewModel)
    fun inject(viewModel: MainViewModel)
    fun inject(viewModel: OrderWhenNetworkErrorViewModel)
    fun inject(viewModel: OfflineConfirmViewModel)
    fun inject(viewModel: AddUserViewModel)

    @Component.Builder
    interface Builder {
        fun build(): ViewModelInjector
        fun networkModule(networkModule: NetworkModule): Builder
        fun applicationComponent(applicationComponent: ApplicationComponent): Builder
        fun repositoryComponent(repository: RepositoryModule): Builder
        fun databaseModule(databaseModule: DatabaseModule): Builder
    }
}
