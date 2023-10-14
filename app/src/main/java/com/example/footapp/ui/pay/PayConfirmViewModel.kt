package com.example.footapp.ui.pay

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.footapp.Request.ConfirmBillRequest
import com.example.footapp.base.BaseViewModel
import com.example.footapp.network.ApiService
import com.example.footapp.repository.CustomerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class PayConfirmViewModel(
    var context: Context,
) : BaseViewModel() {
    @Inject
    lateinit var apiService: ApiService

    @Inject
    lateinit var repository: CustomerRepository

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    fun confirmBill(idBill: Int, status: Int, newPrice: Int) {
        viewModelScope.launch {
            val request = ConfirmBillRequest(idBill, status, newPrice)
            flow {
                emit(apiService.confirmBill(request))
            }.flowOn(Dispatchers.IO)
                .onStart { onRetrievePostListStart() }
                .onCompletion { onRetrievePostListFinish() }
                .catch { }
                .collect {
                    _message.postValue(it.message)
                }
        }
    }
}
