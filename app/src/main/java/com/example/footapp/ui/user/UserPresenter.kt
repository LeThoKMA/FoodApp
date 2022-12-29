package com.example.footapp.ui.user

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.footapp.DAO.DAO
import com.example.footapp.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class UserPresenter(var callback:UserInterface) {
    private var dao=DAO()
    val users= MutableLiveData<List<User>>()
    init {
        getUsers()
    }

    fun addUser(user: User,pass:String,confirmPass:String) {
        if (user.id != -1) {
            if (pass == confirmPass) {
                dao.addUser(user)
                callback.complete()
            } else {
                callback.notify("Mật khẩu không trùng khớp")
            }
        }
        else
        {
            callback.notify("Có lỗi xảy ra")

        }
    }

     fun deleteUser(postion: Int) {
        dao.deleteUser(postion)
         callback.deleteUser(postion)

    }

     fun updateUser(user: User,name:String,salary:String) {
         var map:HashMap<String,Any>? = hashMapOf()
         if(name.isNotBlank())
         {
             map?.put("name",name)
         }
         if(salary.isNotBlank())
         {
             map?.put("salary",salary)
         }
         dao.updateUser(map,user)
         callback.notify("Đã xử lí")
         callback.complete()
    }

    fun getUsers() {

        val itemListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI


                dataSnapshot.getValue<List<User>>()?.let {
                    users.postValue(it)
                }


            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                users.postValue(null)
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())

            }
        }
        dao.userReference.addValueEventListener(itemListener)
    }

}