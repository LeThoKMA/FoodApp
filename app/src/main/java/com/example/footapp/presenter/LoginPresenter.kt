package com.example.footapp.presenter

import android.content.Context
import android.util.Log
import com.example.footapp.DAO.DAO
import com.example.footapp.model.User
import com.example.footapp.MyPreference
import com.example.footapp.`interface`.LoginInterface
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class LoginPresenter(var callBack: LoginInterface, var context: Context) {
    private var dao = DAO()
    private var users: List<User> = listOf()
    lateinit var myPreference: MyPreference

    init {
        getUsers()
        myPreference= MyPreference().getInstance(context)!!
    }

    fun getUsers() {

        val itemListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI


                dataSnapshot.getValue<List<User>>()?.let { users = it }
                Log.e("TAG", users.toString() )


            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())

            }
        }
        dao.userReference.addValueEventListener(itemListener)
    }

    fun signIn(username: String, password: String) {
//        val mAuth = FirebaseAuth.getInstance()
//        if (username.isNotBlank() && password.isNotBlank()) {
//            if(username.contains("@")) {
//                mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                    } else {
//                        callBack.messageLogin("Đăng nhập thất bại")
//                    }
//                }
//            }
//            else
//            {
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

//            }
        //}
    }
}