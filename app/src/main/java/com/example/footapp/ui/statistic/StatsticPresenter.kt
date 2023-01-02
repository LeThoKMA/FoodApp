package com.example.footapp.ui.statistic

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.footapp.DAO.DAO
import com.example.footapp.MyPreference
import com.example.footapp.model.Bill
import com.example.footapp.model.Item
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import java.text.SimpleDateFormat
import java.util.Calendar

class StatsticPresenter(var context: Context) {
    var simpleDateFormat = SimpleDateFormat("dd-MM-yyyy HH-mm")
    var sdf=SimpleDateFormat("dd-MM-yyyy")
    private var myPreference = MyPreference().getInstance(context)
    val dataInMonth = MutableLiveData<HashMap<String, Int>>()
    var dataInYear= MutableLiveData<HashMap<Int,Int>>()
    var dataCheck= MutableLiveData<Boolean>()
    private var bills: List<Bill> = listOf()
    private var dao = DAO()
    var size = 0
    var totalPrice = 0

    init {
        getBills()


    }

    fun getBills() {

        val itemListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI


                dataSnapshot.getValue<List<Bill>>()?.let { bills = it }
                dataCheck.postValue(true)

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())

            }
        }
        dao.billReference.addValueEventListener(itemListener)

    }

    fun getDataByDay(list: List<Bill>): HashMap<String, Int> {
        var map: HashMap<String, Int> = hashMapOf()
        for (item in list) {
            var time=sdf.format(sdf.parse(item.dateTime.toString()))
            if (map.containsKey(time)) {
                var value = map[time]
                var newValue = item.totalPrice?.let { value?.plus(it) }
                newValue?.let { map.put(time, it) }
            } else {
                map.put(time, item.totalPrice ?: 0)
            }
        }
        Log.e("TAG2222", map.toString())
        return map
    }

    fun getDataInMonth(time: String) {
        var list: List<Bill> = bills
            ?.filter { it.dateTime?.contains(time) == true } ?: listOf()
        dataInMonth.postValue(getDataByDay(list))


    }

    fun getTotalInMonth(time: String): Int {
        var price = 0
        var list: List<Bill> = bills
            ?.filter { it.dateTime?.contains(time) == true } ?: listOf()
        list.forEach { price += it.totalPrice ?: 0 }
        return price

    }


    fun getDataInYear() {
        var map:HashMap<Int,Int> = hashMapOf()
          var year=Calendar.YEAR
           for(i in 1 until 13)
           {
               map.put(i,getTotalInMonth(i.toString()+year))
           }
        dataInYear.postValue(map)

    }
}