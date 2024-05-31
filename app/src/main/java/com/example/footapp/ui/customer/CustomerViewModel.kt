package com.example.footapp.ui.customer

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.footapp.base.BaseViewModel
import com.example.footapp.network.ApiService
import com.example.footapp.repository.CustomerRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class CustomerViewModel() : BaseViewModel() {
    @Inject
    lateinit var repository: CustomerRepository

    @Inject
    lateinit var apiService: ApiService

    private val _data = MutableLiveData<List<String>>()
    val data: LiveData<List<String>> = _data

    init {
        getBannerList()
    }

    private fun getBannerList() {
        viewModelScope.launch {
            flow { emit(apiService.getBannerList()) }
                .onStart { onRetrievePostListStart() }
                .onCompletion { onRetrievePostListFinish() }
                .catch { handleApiError(it) }
                .collect {
                    it.data?.let { _data.postValue(it) }
                }
        }
    }
}
