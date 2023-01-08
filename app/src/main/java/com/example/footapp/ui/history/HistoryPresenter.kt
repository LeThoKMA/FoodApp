package com.example.footapp.ui.history

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

class HistoryPresenter() {
    var bill=MutableLiveData<List<Bill>>()
    private var dao = DAO()

    init {
        getBills()
    }
    fun getBills() {

        val itemListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI


                dataSnapshot.getValue<List<Bill>>()?.let {
                    bill.postValue(it)
                }


            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                bill.postValue(null)
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())

            }
        }
        dao.billReference.addValueEventListener(itemListener)

    }
}