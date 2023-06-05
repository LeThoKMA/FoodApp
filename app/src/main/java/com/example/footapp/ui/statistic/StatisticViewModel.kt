package com.example.footapp.ui.statistic

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.footapp.DAO.DAO
import com.example.footapp.MyPreference
import com.example.footapp.base.BaseViewModel
import com.example.footapp.model.Bill
import com.example.footapp.model.ItemStatistic
import com.example.footapp.network.ApiService
import com.google.firebase.database.ktx.getValue
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import javax.inject.Inject

class StatisticViewModel(var context: Context) : BaseViewModel() {
    @Inject
    lateinit var apiService: ApiService
    var sdf = SimpleDateFormat("dd-MM-yyyy")
    private var myPreference = MyPreference().getInstance(context)

    private val _years = MutableLiveData<List<Int>>()
    val year: LiveData<List<Int>> = _years

    private val _dataInDay = MutableLiveData<ItemStatistic>()
    val dataInDay: LiveData<ItemStatistic> = _dataInDay

    private val _dataInYear = MutableLiveData<List<ItemStatistic>>()
    val dataInYear: LiveData<List<ItemStatistic>> = _dataInYear

    var dataCheck = MutableLiveData<Boolean>()
    private var bills: List<Bill> = listOf()
    private var dao = DAO()
    var size = 0
    var totalPrice = 0

    init {
        getYearToStatistic()
        getStatisticInDay()
    }

    private fun getYearToStatistic() {
        viewModelScope.launch {
            flow { emit(apiService.getYearToStatistic()) }
                .onStart { onRetrievePostListStart() }
                .onCompletion { onRetrievePostListFinish() }
                .catch { handleApiError(it) }
                .collect {
                    it.data?.let {
                        _years.postValue(it)
                        getStatisticOfYear(it[it.lastIndex])
                    }
                }
        }
    }

    fun getStatisticOfYear(year: Int) {
        viewModelScope.launch {
            flow { emit(apiService.getYearToStatistic(year)) }
                .onStart { onRetrievePostListStart() }
                .onCompletion { onRetrievePostListFinish() }
                .catch { handleApiError(it) }
                .collect {
                    it.data?.let { _dataInYear.postValue(it) }
                }
        }
    }

    fun getStatisticInDay() {
        viewModelScope.launch {
            flow { emit(apiService.getStatisticInToday()) }
                .onStart { onRetrievePostListStart() }
                .onCompletion { onRetrievePostListFinish() }
                .catch { handleApiError(it) }
                .collect {
                    it.data?.let { _dataInDay.postValue(it) }
                }
        }
    }
}
