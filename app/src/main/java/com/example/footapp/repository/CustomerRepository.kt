package com.example.footapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.footapp.DAO.BannerDao
import com.example.footapp.Response.BillResponse
import com.example.footapp.model.DetailItemChoose
import com.example.footapp.model.dbmodel.BannerDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CustomerRepository @Inject constructor(private val bannerDao: BannerDao) {
    private val dispatcher = Dispatchers.IO
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

    suspend fun insertBanner(url: String) = withContext(dispatcher) {
        bannerDao.insertIfNotExists(BannerDB(url = url))
    }

    suspend fun getAllBanner(): Flow<List<BannerDB>> = withContext(dispatcher) {
        flow { emit(bannerDao.getAllBanner()) }
    }
}
