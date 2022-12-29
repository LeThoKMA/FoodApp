package com.example.footapp.ui.Oder

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.footapp.DAO.DAO
import com.example.footapp.model.Bill
import com.example.footapp.model.Item
import com.example.footapp.model.ItemBill
import com.example.footapp.ui.MyPreference
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import java.text.SimpleDateFormat
import java.util.Calendar

class PayConfirmPresenter(var callback: OderInterface, var context: Context, var activity: CartActivity) {
    var simpleDateFormat = SimpleDateFormat("dd-MM-yyyy mm-HH")
    val dataItems= MutableLiveData<ArrayList<Item?>>()

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

    fun addPrice(id: Int, priceItem: Int, count: Int) {
        map.put(id, priceItem)
        mapItem.put(id.toString(), count)
        var total = 0
        for (item in map) {
            total += item.value
        }
        this.totalPrice = total
        callback.price(total)
    }

    fun payConfirm() {
        if(totalPrice>0)
        {var itemBills: ArrayList<ItemBill> = arrayListOf()

        for (item in mapItem) {
            itemBills.add(ItemBill(item.key.toInt(), item.value))
        }

        for (item in items)
        {
            for (itemBill in itemBills)
            {
                if (item?.id==itemBill.id)
                {
                   var amount= itemBill.count?.let { item?.amount?.minus(it) }
                    var map:HashMap<String,Any> = hashMapOf()
                    if (amount != null) {
                        map.put("amount", amount)
                    }
                    item?.let { dao.updateItem(it,map) }
                }
            }
        }

        var mapItemBill: HashMap<String, ArrayList<ItemBill>>? = hashMapOf()
        mapItemBill?.put("items", itemBills)
        var bill = Bill(
            size,
            myPreference?.getUser()?.id,
            mapItemBill,
            totalPrice,
            simpleDateFormat.format(Calendar.getInstance().time)
        )
        dao.addBill(bill)

        callback.complete("Thành công")
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


                dataSnapshot.getValue<List<Item>>()?.let { list.addAll(it) }
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

//    fun getPrice(){
//        var total=0
//        for(item in map)
//        {
//            total+=item.value
//        }
//        return total
//    }
}