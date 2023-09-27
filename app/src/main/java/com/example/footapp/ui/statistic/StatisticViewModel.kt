package com.example.footapp.ui.statistic

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.footapp.MyPreference
import com.example.footapp.Response.StatisticResponse
import com.example.footapp.base.BaseViewModel
import com.example.footapp.model.ItemStatistic
import com.example.footapp.model.StaffData
import com.example.footapp.network.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import javax.inject.Inject
import kotlin.math.log10
import kotlin.math.pow

class StatisticViewModel(var context: Context) : BaseViewModel() {
    @Inject
    lateinit var apiService: ApiService
    var sdf = SimpleDateFormat("dd-MM-yyyy")
    private var myPreference = MyPreference().getInstance(context)

    private val _years = MutableLiveData<List<Int>>()
    val year: LiveData<List<Int>> = _years

    private val _dataInDay = MutableLiveData<ItemStatistic>()
    val dataInDay: LiveData<ItemStatistic> = _dataInDay

    private val _dataInYear = MutableLiveData<StatisticResponse>()
    val dataInYear: LiveData<StatisticResponse> = _dataInYear

    private val _uiState = MutableStateFlow(State())
    val uiState: StateFlow<State> = _uiState.asStateFlow()
    var size = 0
    var totalPrice = 0

    init {
        getStatisticInDay()
        getStatisticStaff(_uiState.value.month, _uiState.value.yearForStaff)
        getYearToStatistic()
    }

    private fun getYearToStatistic() {
        viewModelScope.launch {
            flow { emit(apiService.getYearToStatistic()) }
                .onStart { }
                .onCompletion { }
                .catch { handleApiError(it) }
                .collect {
                    it.data?.let {
                        _years.postValue(it)
                        getStatisticOfYear(it[it.lastIndex])
                    }
                }
        }
    }

    private suspend fun getStatisticOfYear(year: Int) {
        flow { emit(apiService.getYearToStatistic(year)) }
            .onStart { if (isLoading.value != true) onRetrievePostListStart() }
            .onCompletion { onRetrievePostListFinish() }
            .catch { handleApiError(it) }
            .collect {
                it.data?.let { data ->
                    _uiState.update {
                        it.copy(
                            stoneValue = calculateRangeOfBar(data.maxValue),
                            barDatas = data.values,
                            total = data.total,
                        )
                    }
                }
            }
    }

    private fun getStatisticInDay() {
        viewModelScope.launch {
            flow { emit(apiService.getStatisticInToday()) }
                .onStart { onRetrievePostListStart() }
                .onCompletion { }
                .catch { handleApiError(it) }
                .collect {
                    it.data?.let { _dataInDay.postValue(it) }
                }
        }
    }

    private fun getStatisticStaff(month: Int, year: Int) {
        viewModelScope.launch {
            flow { emit(apiService.getStatisticStaff(month, year)) }
                .onStart { onRetrievePostListStart() }
                .onCompletion { onRetrievePostListFinish() }
                .catch { handleApiError(it) }
                .collect { data ->
                    val tmpList = mutableListOf<StaffData>()
                    tmpList.add(StaffData())
                    data.data?.let { tmpList.addAll(it) }
                    data.data?.let { _uiState.update { it.copy(staffList = tmpList) } }
                }
        }
    }

    fun doGetStatisticInYear(year: Int) {
        viewModelScope.launch { getStatisticOfYear(year) }
    }

    fun calculateRangeOfBar(maxValue: Int): Int {
        val expValue = log10(maxValue.toDouble()).toInt()
        val stoneValue =
            maxValue.div(10.0.pow(expValue.toDouble())).toInt()
                .times(10.0.pow(expValue.toDouble())).toInt()
        return stoneValue.div(5)
    }

    data class State(
        val stoneValue: Int = 0,
        val barDatas: List<ItemStatistic> = listOf(),
        val total: Int = 0,
        val month: Int = Calendar.getInstance().get(Calendar.MONTH).plus(1),
        val yearForStaff: Int = Calendar.getInstance().get(Calendar.YEAR),
        val staffList: MutableList<StaffData> = mutableListOf(),
    )
}
