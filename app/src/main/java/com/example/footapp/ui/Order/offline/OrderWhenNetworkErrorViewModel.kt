package com.example.footapp.ui.Order.offline

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.footapp.Response.CategoryResponse
import com.example.footapp.base.BaseViewModel
import com.example.footapp.model.DetailItemChoose
import com.example.footapp.model.Item
import com.example.footapp.network.ApiService
import com.example.footapp.repository.CustomerRepository
import com.example.footapp.ui.Order.HomeRepository
import com.example.footapp.utils.fromString
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class OrderWhenNetworkErrorViewModel : BaseViewModel() {
    @Inject
    lateinit var apiService: ApiService

    @Inject
    lateinit var repository: CustomerRepository

    @Inject
    lateinit var homeRepository: HomeRepository
    var dataItems = MutableLiveData<MutableList<Item>>()
        private set

    private val _price = MutableLiveData<Int>()
    val price: LiveData<Int> = _price

    private val _category = MutableLiveData<List<CategoryResponse>?>()
    val category: MutableLiveData<List<CategoryResponse>?> = _category
    val message = MutableLiveData<String>()

    private var mapDetailItemChoose: HashMap<Int, DetailItemChoose> = hashMapOf()
    var list: ArrayList<Item?> = arrayListOf()
    var size = 0
    var totalPrice = 0


    init {
        fetchTypeFromDb()
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
            _price.postValue(total)
        }
    }

    private fun fetchTypeFromDb() {
        viewModelScope.launch {
            homeRepository.getAllType()
                .onStart { onRetrievePostListStart() }
                .map { it.map { typeDb -> CategoryResponse(typeDb.id, typeDb.name) } }
                .combine(
                    homeRepository.getAllItem().map {
                        it.map { itemDb ->
                            Item(
                                itemDb.id,
                                itemDb.name,
                                itemDb.price,
                                itemDb.amount,
                                fromString(itemDb.imgUrl ?: "")
                            )
                        }
                    }, transform = { it1, it2 -> Pair(it1, it2) })
                .catch { message.postValue(it.message) }
                .onCompletion { onRetrievePostListFinish() }
                .collect {
                    _category.postValue(it.first)
                    dataItems.postValue(it.second.toMutableList())
                }
        }
    }

    fun resetData() {
        mapDetailItemChoose.clear()
        totalPrice = 0
    }
}
