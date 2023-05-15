package com.example.footapp.di

import com.example.footapp.presenter.CustomerViewModel
import com.example.footapp.presenter.LoginViewModel
import com.example.footapp.presenter.OrderViewModel
import dagger.Component

@Component(
    modules = [NetworkModule::class, RepositoryModule::class],
    dependencies = [ApplicationComponent::class],
)
@ViewModelScope
interface ViewModelInjector {
    fun inject(viewModel: LoginViewModel)
    fun inject(viewModel: OrderViewModel)
    fun inject(viewModel: CustomerViewModel)

    @Component.Builder
    interface Builder {
        fun build(): ViewModelInjector
        fun networkModule(networkModule: NetworkModule): Builder
        fun applicationComponent(applicationComponent: ApplicationComponent): Builder
        fun repositoryComponent(repository: RepositoryModule): Builder
    }
}
