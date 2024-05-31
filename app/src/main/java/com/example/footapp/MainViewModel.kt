package com.example.footapp

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.footapp.base.BaseViewModel

class MainViewModel() : BaseViewModel() {
    private val _paySuccess = MutableLiveData<Unit>()
    val paySuccess: LiveData<Unit> = _paySuccess

    private val _dataToPay = MutableLiveData<Bundle>()
    val dataToPay: LiveData<Bundle> = _dataToPay

    private val _refreshData = MutableLiveData<Unit>()
    val refreshData: LiveData<Unit> = _refreshData

    fun onPaySuccess() {
        _paySuccess.postValue(Unit)
    }

    fun gotoPayFragment(bundle: Bundle) {
        _dataToPay.postValue(bundle)
    }

    fun onRefreshDataInOrderList() {
        _refreshData.postValue(Unit)
    }
}
