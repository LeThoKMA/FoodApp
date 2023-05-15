package com.example.footapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.footapp.model.DetailItemChoose

class CustomerRepository {
    private val _data = MutableLiveData<DetailItemChoose>()
    val data: LiveData<DetailItemChoose> = _data

    fun sendData(item: DetailItemChoose) {
        _data.postValue(item)
    }

}
