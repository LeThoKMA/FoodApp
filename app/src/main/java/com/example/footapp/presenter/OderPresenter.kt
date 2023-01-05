package com.example.footapp.presenter

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.footapp.DAO.DAO
import com.example.footapp.model.DetailItemChoose
import com.example.footapp.model.Item
import com.example.footapp.MyPreference
import com.example.footapp.ui.Oder.CartActivity
import com.example.footapp.`interface`.OderInterface
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import java.text.SimpleDateFormat

class OderPresenter(var callback: OderInterface, var context: Context, var activity: CartActivity) {
    var simpleDateFormat = SimpleDateFormat("dd-MM-yyyy mm-HH")
    val dataItems= MutableLiveData<ArrayList<Item?>>()
    var mapDetailItemChoose:HashMap<Int,DetailItemChoose> = hashMapOf()

    private var map: HashMap<Int, Int> = hashMapOf()
    private var mapItem: HashMap<String, Int> = hashMapOf()
    private var myPreference = MyPreference().getInstance(context)
    private var items: List<Item?> = arrayListOf()
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




    fun getItems()
    {
        val list: ArrayList<Item?> = arrayListOf()
        val itemListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI


                dataSnapshot.getValue<List<Item>>()?.let {
                    for(item in it)
                    {
                        if(item!=null)
                        {
                            list.add(item) }

                    }
                    }
                dataItems.postValue(list)


            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
                dataItems.postValue(null)
            }
        }
        dao.itemReference.addValueEventListener(itemListener)

    }


}