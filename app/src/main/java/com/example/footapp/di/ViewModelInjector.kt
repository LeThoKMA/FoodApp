package com.example.footapp.di

import androidx.lifecycle.ViewModel
import com.example.footapp.presenter.LoginViewModel
import dagger.Component

@Component(modules = [NetworkModule::class], dependencies = [ApplicationComponent::class])
@ViewModelScope
interface ViewModelInjector {
    fun inject(viewModel: LoginViewModel)

    @Component.Builder
    interface Builder {
        fun build(): ViewModelInjector
        fun networkModule(networkModule: NetworkModule): Builder
        fun applicationComponent(applicationComponent: ApplicationComponent): Builder
    }
}
