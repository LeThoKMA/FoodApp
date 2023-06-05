package com.example.footapp.ui.orderlist

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.footapp.Response.BillDetailResponse
import com.example.footapp.base.BaseViewModel
import com.example.footapp.model.OrderItem
import com.example.footapp.network.ApiService
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class OrderListViewModel(val context: Context) : BaseViewModel() {
    @Inject
    lateinit var apiService: ApiService

    private val _orderList = MutableLiveData<List<OrderItem>>()
    val orderList: LiveData<List<OrderItem>> = _orderList

    private val _orderDetail = MutableLiveData<BillDetailResponse>()
    val orderDetail: LiveData<BillDetailResponse> = _orderDetail

    var page = 1

    init {
        fetchOrderList()
    }

    fun fetchOrderList() {
        viewModelScope.launch {
            flow { emit(apiService.getOrderList(page)) }
                .onStart { onRetrievePostListStart() }
                .onCompletion {
                    onRetrievePostListFinish()
                    page++
                }
                .catch { handleApiError(it) }
                .collect {
                    it.data?.let { _orderList.postValue(it) }
                }
        }
    }

    fun getOrderDetail(id: Int) {
        viewModelScope.launch {
            flow { emit(apiService.getOrderDetail(id)) }
                .onStart { onRetrievePostListStart() }
                .onCompletion { onRetrievePostListFinish() }
                .collect {
                    it.data?.let {
                        _orderDetail.postValue(it)
                    }
                }
        }
    }
}
