package com.example.footapp.presenter

import android.content.Context
import android.util.Log
import com.example.footapp.DAO.DAO
import com.example.footapp.model.User
import com.example.footapp.MyPreference
import com.example.footapp.interface1.LoginInterface
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class LoginPresenter(var callBack: LoginInterface, var context: Context) {
    private var dao = DAO()
    private var users: ArrayList<User> = arrayListOf()
    lateinit var myPreference: MyPreference

    init {

        myPreference= MyPreference().getInstance(context)!!
    }

    fun getUsers() {


    }

    fun signIn(username: String, password: String) {
        dao.userReference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {


                val item = snapshot.getValue(
                    User::class.java
                )

                if (item != null) {
                    users.add(item)
                }

                validUser(username,password,users)

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