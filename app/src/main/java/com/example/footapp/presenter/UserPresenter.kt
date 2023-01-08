package com.example.footapp.presenter

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.footapp.DAO.DAO
import com.example.footapp.interface1.UserInterface
import com.example.footapp.model.User
import com.google.firebase.database.*

class UserPresenter(var callback: UserInterface) {
    private var dao = DAO()
    val users = MutableLiveData<MutableList<User>>()
    var updateData=MutableLiveData<User>()
    var list :ArrayList<User> = arrayListOf()
    init {
     getDataItem()
    }

    fun addUser(user: User, pass: String, confirmPass: String) {
        if (user.id != -1) {
            if (pass == confirmPass) {
                dao.addUser(user)
                callback.complete()
            } else {
                callback.notify("Mật khẩu không trùng khớp")
            }
        } else {
            callback.notify("Có lỗi xảy ra")

        }
    }

    fun deleteUser(postion: Int, user: User) {
        user.id?.let { dao.deleteUser(it) }
        callback.deleteUser(postion)
        list.removeAt(postion)

    }

    fun updateUser(user: User) {

      dao.addUser(user)
        callback.notify("Đã xử lí")
        callback.complete()
    }



    fun getDataItem() {
       dao.userReference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {


                val item = snapshot.getValue(
                    User::class.java
                )

                if (item != null) {
                    list.add(item)
                }
                Log.e("aaaa", list.toString())
                users.postValue(list)

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val item = snapshot.getValue(
                    User::class.java
                )
                if (item != null) {
                    updateData.postValue(item)
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })
    }


}