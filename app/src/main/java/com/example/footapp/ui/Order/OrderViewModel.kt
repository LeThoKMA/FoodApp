package com.example.footapp.ui.Order

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.footapp.DAO.DAO
import com.example.footapp.Response.BillResponse
import com.example.footapp.base.BaseViewModel
import com.example.footapp.model.DetailItemChoose
import com.example.footapp.model.Item
import com.example.footapp.model.ItemBillRequest
import com.example.footapp.network.ApiService
import com.example.footapp.repository.CustomerRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class OrderViewModel(
    val context: Context,
) : BaseViewModel() {
    @Inject
    lateinit var apiService: ApiService

    @Inject
    lateinit var repository: CustomerRepository
    val dataItems = MutableLiveData<ArrayList<Item>>()

    private val _price = MutableLiveData<Int>()
    val price: LiveData<Int> = _price

    private val _confirm = MutableLiveData<BillResponse?>()
    val confirm: LiveData<BillResponse?> = _confirm

    val message = MutableLiveData<String>()

    var mapDetailItemChoose: HashMap<Int, DetailItemChoose> = hashMapOf()
    var list: ArrayList<Item?> = arrayListOf()
    private var dao = DAO()
    var size = 0
    var totalPrice = 0

    init {
        fetchItems()
    }

    fun addItemToBill(item: DetailItemChoose) {
        if (!item.flag!!) {
            val itemRemove = DetailItemChoose(item.id, item.name, 0, 0, 0, null, false)
            mapDetailItemChoose.put(itemRemove.id ?: 0, itemRemove)
        } else {
            mapDetailItemChoose.put(item.id ?: 0, item)
        }
        var total = 0
        for (item in mapDetailItemChoose) {
            total += item.value.totalPrice ?: 0
        }
        this.totalPrice = total
        _price.postValue(total)
        // callback.price(total)
    }

    fun payConfirm(list: List<DetailItemChoose>) {
        if (totalPrice > 0) {
            viewModelScope.launch {
                val bilLRequest: MutableList<ItemBillRequest> = mutableListOf()
                list.forEach { bilLRequest.add(ItemBillRequest(it.id, it.count, it.totalPrice)) }
                flow { emit(apiService.makeBill(bilLRequest)) }
                    .onStart { onRetrievePostListStart() }
                    .onCompletion {
                        onRetrievePostListFinish()
                    }
                    .catch { Log.e("TAG", it.toString()) }
                    .collect {
                        if (it.data != null) {
                            _confirm.postValue(it.data)
                        }
                    }
            }
        } else {
            message.postValue("Vui lòng chọn đồ uống trước")
        }
    }

    fun fetchItems() {
        viewModelScope.launch {
            flow {
                emit(apiService.getItems())
            }.onStart { onRetrievePostListStart() }
                .onCompletion { onRetrievePostListFinish() }
                .collect {
                    if (it.data != null) {
                        dataItems.postValue(it.data as ArrayList<Item>?)
                    }
                }
        }
    }
}
