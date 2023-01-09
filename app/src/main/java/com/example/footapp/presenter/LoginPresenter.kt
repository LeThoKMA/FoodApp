package com.example.footapp.presenter

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.footapp.DAO.DAO
import com.example.footapp.model.User
import com.example.footapp.MyPreference
import com.example.footapp.interface1.LoginInterface
import com.example.footapp.model.Bill
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class LoginPresenter(var callBack: LoginInterface, var context: Context) {
    private var dao = DAO()
    var usersData= MutableLiveData<List<User>>()
    var list:ArrayList<User> = arrayListOf()
     var myPreference: MyPreference

    init {

        myPreference= MyPreference().getInstance(context)!!
        signIn()
    }



    fun signIn() {

        dao.userReference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {


                val item = snapshot.getValue(
                    User::class.java
                )

                if (item != null) {
                    list.add(item)
                }
                usersData.postValue(list)

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val item = snapshot.getValue(
                    User::class.java
                )

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })

    }
    fun validUser(username: String, password: String, users: List<User>)
    {
        var flag=false
        for(user in users)
        {

            if(username == user.name)
            {
                if(password == user.password)
                {
                    myPreference.saveUser(user.id.toString(),username.toString(),user.password.toString(),user.salary.toString(),user.admin?:0)
                    callBack.loginSuccess()
                }
                else{
                    callBack.messageLogin("Mật khẩu không chính xác")
                }
                flag=true
                break

            }
        }
        if(!flag)
        {
            callBack.messageLogin("Tài khoản không tồn tại")

        }
    }
}