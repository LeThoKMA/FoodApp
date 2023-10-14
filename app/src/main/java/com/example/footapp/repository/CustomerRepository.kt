package com.example.footapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.footapp.Response.BillResponse
import com.example.footapp.model.DetailItemChoose

class CustomerRepository {
    private val _data = MutableLiveData<DetailItemChoose>()
    val data: LiveData<DetailItemChoose> = _data

    private val _billResponse = MutableLiveData<BillResponse>()
    val billResponse: LiveData<BillResponse> = _billResponse

    private val _resetData = MutableLiveData<Boolean>()
    val resetData: LiveData<Boolean> = _resetData

    fun sendData(item: DetailItemChoose) {
        _data.postValue(item)
    }

    fun getBillResponse(billResponse: BillResponse) {
        _billResponse.postValue(billResponse)
    }

    fun resetData() {
        _resetData.postValue(true)
    }
}
