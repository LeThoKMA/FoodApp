package com.example.footapp.ui.Order.offline

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.footapp.Response.BillResponse
import com.example.footapp.Response.CategoryResponse
import com.example.footapp.base.BaseViewModel
import com.example.footapp.model.DetailItemChoose
import com.example.footapp.model.Item
import com.example.footapp.network.ApiService
import com.example.footapp.repository.CustomerRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class OrderWhenNetworkErrorViewModel : BaseViewModel() {
    @Inject
    lateinit var apiService: ApiService

    @Inject
    lateinit var repository: CustomerRepository
    val dataItems = MutableLiveData<ArrayList<Item>>()

    private val _price = MutableSharedFlow<Int>()
    val price: SharedFlow<Int> = _price

    private val _category = MutableLiveData<List<CategoryResponse>?>()
    val category: MutableLiveData<List<CategoryResponse>?> = _category

    private val _confirm = Channel<Event>(Channel.BUFFERED)
    val confirm = _confirm.receiveAsFlow()

    val message = MutableLiveData<String>()

    private var mapDetailItemChoose: HashMap<Int, DetailItemChoose> = hashMapOf()
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

    sealed class Event {
        data class OnConfirmSuccess(val response: BillResponse) : Event()
    }
}
