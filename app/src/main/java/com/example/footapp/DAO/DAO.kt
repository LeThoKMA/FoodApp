package com.example.footapp.DAO

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.footapp.model.Bill
import com.example.footapp.model.Item
import com.example.footapp.model.User
import com.example.footapp.utils.API_DATABASE
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue

class DAO {
    private var database: DatabaseReference
    val bills = MutableLiveData<ArrayList<Bill?>>()

    init {
        database = FirebaseDatabase.getInstance(API_DATABASE).reference
    }

    val reference: DatabaseReference
        get() {
            return FirebaseDatabase.getInstance(API_DATABASE).reference
        }
    val userReference: DatabaseReference
        get() {
            return FirebaseDatabase.getInstance(API_DATABASE).reference.child("users")
        }
    val itemReference: DatabaseReference
        get() {
            return FirebaseDatabase.getInstance(API_DATABASE).reference.child("items")
        }

    val billReference: DatabaseReference
        get() {

            return FirebaseDatabase.getInstance(API_DATABASE).reference.child("bills")
        }

    fun addUser(user: User): Boolean {
        return try {
            database.child("users").child(user.id.toString()).setValue(user)

            true
        } catch (e: Exception) {
            Log.e("Exception", e.toString())
            false
        }
    }

    fun updateUser(map: HashMap<String, Any>?, user: User): Boolean {
        return try {
            map?.let { database.child("users").child(user.id.toString()).updateChildren(it) }
            true
        } catch (e: Exception) {
            Log.e("Exception", e.toString())
            false
        }
    }

    fun addItem(item: Item): Boolean {
        return try {
            database.child("items").child(item.id.toString()).setValue(item)
            true
        } catch (e: Exception) {
            Log.e("Exception", e.toString())
            false
        }
    }

    fun updateItem(item: Item, map: HashMap<String, Any>?) {
        try {
            map?.let { database.child("items").child(item.id.toString()).updateChildren(it) }
        } catch (e: Exception) {
            Log.e("Exception", e.toString())
        }
    }

    fun addBill(bill: Bill): Boolean {
        return try {
            database.child("bills").child(bill.idBill.toString()).setValue(bill)
            true
        } catch (e: Exception) {
            Log.e("Exception", e.toString())
            false
        }
    }

    fun getBills() {
        val list: ArrayList<Bill?> = arrayListOf()
        database.child("bills").get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.value.toString().isNotBlank()) {
                Log.e("TAG", dataSnapshot.getValue().toString())
                dataSnapshot.getValue<List<Bill>>()?.let { list.addAll(it) }
                bills.postValue(list)
            } else {
                bills.postValue(null)
            }
        }
            .addOnFailureListener { }
    }

    fun deleteUser(pos: Int) {
        database.child("users").child(pos.toString() + "").removeValue()
    }
}
