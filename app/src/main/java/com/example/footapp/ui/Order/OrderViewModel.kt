package com.example.footapp.ui.Order

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.footapp.Response.BillResponse
import com.example.footapp.Response.CategoryResponse
import com.example.footapp.base.BaseViewModel
import com.example.footapp.model.DetailItemChoose
import com.example.footapp.model.Item
import com.example.footapp.model.ItemBillRequest
import com.example.footapp.network.ApiService
import com.example.footapp.repository.CustomerRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
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

    private val _price = MutableSharedFlow<Int>()
    val price: SharedFlow<Int> = _price

    private val _category = MutableLiveData<List<CategoryResponse>?>()
    val category: MutableLiveData<List<CategoryResponse>?> = _category

    private val _confirm = MutableStateFlow(BillResponse())
    val confirm: StateFlow<BillResponse> = _confirm

    val message = MutableLiveData<String>()

    var mapDetailItemChoose: HashMap<Int, DetailItemChoose> = hashMapOf()
    var list: ArrayList<Item?> = arrayListOf()
    var size = 0
    var totalPrice = 0

    init {
        fetchItems()
    }

    fun addItemToBill(item: DetailItemChoose) {
        viewModelScope.launch {
            if (item.flag == null || !item.flag!!) {
                val itemRemove = DetailItemChoose(item.id, item.name, 0, 0, 0, null, false)
                mapDetailItemChoose.put(itemRemove.id ?: 0, itemRemove)
            } else {
                mapDetailItemChoose.put(item.id ?: 0, item)
            }
            var total = 0
            for (item1 in mapDetailItemChoose) {
                total += item1.value.totalPrice ?: 0
            }
            totalPrice = total
            _price.emit(total)
        }
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
                            _confirm.value = it.data
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
                emit(Pair(apiService.getItems(), apiService.getCategory()))
            }.onStart { onRetrievePostListStart() }
                .onCompletion { onRetrievePostListFinish() }
                .collect { pairs ->
                    dataItems.postValue(pairs.first.data as ArrayList<Item>?)
                    _category.postValue(pairs.second.data)
                }
        }
    }

    fun getProductByType(id: Int) {
        viewModelScope.launch {
            flow { emit(apiService.getProductByType(id)) }
                .onStart { onRetrievePostListStart() }
                .onCompletion { onRetrievePostListFinish() }
                .collect {
                    dataItems.postValue(it.data as ArrayList<Item>?)
                }
        }
    }
}
