package com.example.footapp.ui.orderlist

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.footapp.Request.ConfirmBillRequest
import com.example.footapp.Response.BillDetailResponse
import com.example.footapp.base.BaseViewModel
import com.example.footapp.model.OrderItem
import com.example.footapp.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class OrderListViewModel() : BaseViewModel() {
    @Inject
    lateinit var apiService: ApiService

    private val _orderList = MutableLiveData<List<OrderItem>>()
    val orderList: LiveData<List<OrderItem>> = _orderList

    private val _orderListStatus = MutableLiveData<List<OrderItem>>()
    val orderListStatus: LiveData<List<OrderItem>> = _orderListStatus

    private val _orderDetail = MutableLiveData<BillDetailResponse>()
    val orderDetail: LiveData<BillDetailResponse> = _orderDetail

    private val _messageConfirm = MutableLiveData<String>()
    val messageConfirm: LiveData<String> = _messageConfirm

    private val _messageCancel = MutableLiveData<String>()
    val messageCancel: LiveData<String> = _messageCancel

    private var totalPage = 0

    init {
        fetchOrderList(1, "latest", null)
    }

    fun fetchOrderList(page: Int, time: String?, status: Int?) {
        if (page <= totalPage) {
            viewModelScope.launch {
                flow { emit(apiService.getOrderList(page, time, status)) }
                    .onStart { }
                    .onCompletion {
                    }
                    .catch { handleApiError(it) }
                    .collect {
                        it.data?.let {
                            totalPage = it.totalPage
                            _orderList.postValue(it.billResponses)
                        }
                    }
            }
        }
    }

    fun fetchOrderListWithStatus(page: Int, time: String?, status: Int?) {
        if (page <= totalPage) {
            viewModelScope.launch {
                flow { emit(apiService.getOrderList(page, time, status)) }
                    .onStart { onRetrievePostListStart() }
                    .onCompletion {
                        onRetrievePostListFinish()
                    }
                    .catch { handleApiError(it) }
                    .collect {
                        it.data?.let {
                            totalPage = it.totalPage
                            _orderListStatus.postValue(it.billResponses)
                        }
                    }
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
                    _messageConfirm.postValue(it.message.toString())
                }
        }
    }

    fun cancelBill(idBill: Int, status: Int, newPrice: Int) {
        viewModelScope.launch {
            val request = ConfirmBillRequest(idBill, status, newPrice)
            flow {
                emit(apiService.confirmBill(request))
            }.flowOn(Dispatchers.IO)
                .onStart { onRetrievePostListStart() }
                .onCompletion { onRetrievePostListFinish() }
                .catch { }
                .collect {
                    _messageCancel.postValue(it.message.toString())
                }
        }
    }

    fun resetTotalPage(){
        totalPage = 0
    }
}
