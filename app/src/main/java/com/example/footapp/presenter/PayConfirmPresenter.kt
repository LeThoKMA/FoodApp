package com.example.footapp.presenter

import android.content.Context
import android.util.Log
import com.example.footapp.DAO.DAO
import com.example.footapp.model.Bill
import com.example.footapp.model.DetailItemChoose
import com.example.footapp.model.Item
import com.example.footapp.model.ItemBill
import com.example.footapp.MyPreference
import com.example.footapp.interface1.PayConfirmInterface
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import java.text.SimpleDateFormat
import java.util.Calendar

class PayConfirmPresenter(
    var callback: PayConfirmInterface,
    var context: Context,
) {
    var simpleDateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm")
    private var myPreference = MyPreference().getInstance(context)
    private var items: List<Item?> = arrayListOf()
    private var dao = DAO()
    var size = 0


    init {


        getBills()
        getItems()

    }


    fun payConfirm(map: HashMap<Int, DetailItemChoose>, passwd: String,totalPrice:Int) {
        if (!checkPassword(passwd)) {
            return
        }
        var itemBills: ArrayList<ItemBill> = arrayListOf()

        for (item in map) {
            itemBills.add(ItemBill(item.key, item.value.name.toString(),item.value.count,item.value.price))
        }

        for (item in items) {
            for (itemBill in itemBills) {
                if (item?.id == itemBill.id) {
                    var amount = itemBill.count?.let { item?.amount?.minus(it) }
                    var map: HashMap<String, Any> = hashMapOf()
                    if (amount != null) {
                        map.put("amount", amount)
                    }
                    item?.let { dao.updateItem(it, map) }
                }
            }
        }

        var mapItemBill: HashMap<String, ArrayList<ItemBill>>? = hashMapOf()
        mapItemBill?.put("items", itemBills)
        var bill = Bill(
            size,
            myPreference?.getUser()?.id,
            myPreference?.getUser()?.name,
            mapItemBill,
            totalPrice,

            simpleDateFormat.format(Calendar.getInstance().time)
        )
        dao.addBill(bill)

        callback.complete("Thành công",true)
    }


    fun getItems() {

        val itemListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI


                dataSnapshot.getValue<List<Item>>()?.let { items = it }


            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())

            }
        }
        dao.itemReference.addValueEventListener(itemListener)

    }

    fun checkPassword(passwd: String): Boolean {
        var user = myPreference?.getUser()
        if (passwd == user?.password) {
            return true
        } else {
            callback.complete("Mật khẩu không chính xách",false)
        }
        return false
    }

    fun getBills() {

        val itemListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI


                dataSnapshot.getValue<List<Bill>>()?.let { size = it.size }


            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())

            }
        }
        dao.billReference.addValueEventListener(itemListener)

    }

}