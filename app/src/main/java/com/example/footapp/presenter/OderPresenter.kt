package com.example.footapp.presenter

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.footapp.DAO.DAO
import com.example.footapp.model.DetailItemChoose
import com.example.footapp.model.Item
import com.example.footapp.ui.Oder.CartActivity
import com.example.footapp.interface1.OderInterface
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import java.text.SimpleDateFormat

class OderPresenter(var callback: OderInterface, var context: Context, var activity: CartActivity) {
    var simpleDateFormat = SimpleDateFormat("dd-MM-yyyy HH-mm")
    val dataItems= MutableLiveData<ArrayList<Item?>>()
    var dataChange = MutableLiveData<Item>()
    var mapDetailItemChoose:HashMap<Int,DetailItemChoose> = hashMapOf()
    var list: ArrayList<Item?> = arrayListOf()
    private var dao = DAO()
    var size = 0
    var totalPrice = 0

    init {


        dao.getBills()
        dao.bills.observe(activity) {
            if (it != null) {
                size = it.size
            }
        }

    }



    fun addItemToBill(item:DetailItemChoose) {
        mapDetailItemChoose.put(item.id?:0, item)
        var total = 0
        for (item in mapDetailItemChoose) {
            total += item.value.totalPrice?:0
        }
        this.totalPrice = total
        callback.price(total)
    }
    fun payConfirm()
    {
        if(totalPrice>0)
        {
            callback.confirm(this.mapDetailItemChoose,this.totalPrice)
        }
        else
        {

            callback.complete("Vui lòng chọn đồ uống trước")

        }
    }




    fun getDataItem() {
        dao.itemReference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {


                val item = snapshot.getValue(
                    Item::class.java
                )

                if (item != null) {
                    list.add(item)
                }
                Log.e("aaaa", list.toString())
                dataItems.postValue(list)

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val item = snapshot.getValue(
                    Item::class.java
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