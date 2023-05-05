package com.example.footapp.presenter

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.footapp.BaseViewModel
import com.example.footapp.DAO.DAO
import com.example.footapp.model.DetailItemChoose
import com.example.footapp.model.Item
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

class OrderViewModel(
    val context: Context,
) : BaseViewModel() {
    val dataItems = MutableLiveData<ArrayList<Item?>>()
    var dataChange = MutableLiveData<Item>()

    private val _price = MutableLiveData<Int>()
    val price: LiveData<Int> = _price

    private val _confirm = MutableLiveData<Pair<HashMap<Int, DetailItemChoose>, Int>>()
    val confirm: LiveData<Pair<HashMap<Int, DetailItemChoose>, Int>> = _confirm

    val message = MutableLiveData<String>()

    var mapDetailItemChoose: HashMap<Int, DetailItemChoose> = hashMapOf()
    var list: ArrayList<Item?> = arrayListOf()
    private var dao = DAO()
    var size = 0
    var totalPrice = 0

    init {

        dao.getBills()
    }

    fun addItemToBill(item: DetailItemChoose) {
        mapDetailItemChoose.put(item.id ?: 0, item)
        var total = 0
        for (item in mapDetailItemChoose) {
            total += item.value.totalPrice ?: 0
        }
        this.totalPrice = total
        _price.postValue(total)
        // callback.price(total)
    }

    fun payConfirm() {
        if (totalPrice > 0) {
            var map: HashMap<Int, DetailItemChoose> = hashMapOf()
            for (item in this.mapDetailItemChoose) {
                if (item.value.count!! > 0) {
                    map.put(item.key, item.value)
                }
            }
            _confirm.postValue(Pair(map, this.totalPrice))
        } else {
            message.postValue("Vui lòng chọn đồ uống trước")
            //   callback.complete("Vui lòng chọn đồ uống trước")
        }
    }

    fun getDataItem() {
        dao.itemReference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val item = snapshot.getValue(
                    Item::class.java,
                )

                if (item != null) {
                    list.add(item)
                }
                dataItems.postValue(list)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val item = snapshot.getValue(
                    Item::class.java,
                )
                if (item != null) {
                    dataChange.postValue(item)
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })
    }
}
