package com.example.footapp.ui.Order

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.footapp.Response.BillResponse
import com.example.footapp.Response.CategoryResponse
import com.example.footapp.base.BaseViewModel
import com.example.footapp.model.DetailItemChoose
import com.example.footapp.model.Item
import com.example.footapp.model.ItemBillRequest
import com.example.footapp.model.dbmodel.ItemDB
import com.example.footapp.model.dbmodel.TypeDB
import com.example.footapp.network.ApiService
import com.example.footapp.repository.CustomerRepository
import com.example.footapp.utils.fromList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class OrderViewModel(
) : BaseViewModel() {
    @Inject
    lateinit var apiService: ApiService

    @Inject
    lateinit var repository: CustomerRepository

    @Inject
    lateinit var homeRepository: HomeRepository

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
        fetchQrDefault()
        repository.resetData()
    }

    fun addItemToBill(item: DetailItemChoose) {
        viewModelScope.launch {
            mapDetailItemChoose.put(item.id, item)
            var total = 0
            for (item1 in mapDetailItemChoose) {
                total += item1.value.totalPrice ?: 0
            }
            totalPrice = total
            _price.emit(total)
        }
    }

    fun removeItemInBill(id: Int) {
        viewModelScope.launch {
            val detailItemChoose = mapDetailItemChoose[id]
            mapDetailItemChoose.remove(id)
            totalPrice -= detailItemChoose?.totalPrice ?: 0
            _price.emit(totalPrice)
        }
    }

    fun payConfirm(list: List<DetailItemChoose>) {
        if (totalPrice > 0) {
            viewModelScope.launch {
                homeRepository.confirmBill(list = list)
                    .onStart { onRetrievePostListStart() }
                    .onCompletion {
                        onRetrievePostListFinish()
                    }
                    .catch { Log.e("TAG", it.toString()) }
                    .collect {
                        it.data?.let { response ->
                            _confirm.send(Event.OnConfirmSuccess(response))
                        }
                    }
            }
        } else {
            message.postValue("Vui lòng chọn đồ uống trước")
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun fetchItems() {
        viewModelScope.launch {
            homeRepository.fetchItems().onStart { onRetrievePostListStart() }
                .onCompletion { onRetrievePostListFinish() }
                .onEach { pair ->
                    pair.second.data?.forEach {
                        homeRepository.insertType(TypeDB(it.id, it.name))
                    }
                    pair.first.data?.forEach {
                        homeRepository.insertItem(
                            ItemDB(
                                id = it.id,
                                name = it.name,
                                price = it.price,
                                amount = it.amount,
                                imgUrl = fromList(it.imgUrl ?: emptyList()),
                                typeId = it.category?.id ?: 0
                            )
                        )
                    }
                }.flowOn(Dispatchers.IO)
                .collect { pairs ->
                    dataItems.postValue(pairs.first.data as ArrayList<Item>?)
                    _category.postValue(pairs.second.data)
                }
        }
    }

    fun getProductByType(id: Int) {
        viewModelScope.launch {
            if (id == 0) {
                homeRepository.fetchItems().catch { handleApiError(it) }.collect { pairs ->
                    dataItems.postValue(pairs.first.data as ArrayList<Item>?)
                }
            } else {
                homeRepository.getProductByType(id)
                    .catch { handleApiError(it) }
                    .collect {
                        dataItems.postValue(it.data as ArrayList<Item>?)
                    }
            }
        }
    }

    private fun fetchQrDefault() {
        viewModelScope.launch {
            homeRepository.getQrDefault().collect {
                it.data?.let { it1 -> homeRepository.insertQr(it1) }
            }
        }
    }

    sealed class Event {
        data class OnConfirmSuccess(val response: BillResponse) : Event()
    }
}
