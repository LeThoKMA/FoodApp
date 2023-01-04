package com.example.footapp.ui.user

import com.example.footapp.DAO.DAO
import com.example.footapp.model.User
import java.util.Objects

class UserPresenter(var callback:UserInterface) {
    private var dao=DAO()


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
    }

     fun updateUser(user: User,name:String,salary:String) {
         var map:HashMap<String,Any> = hashMapOf()
         if(name.isNotBlank())
         {
             map.put("name",name)
         }
         if(salary.isNotBlank())
         {
             map.put("salary",salary)
         }
         dao.updateUser(map,user)
         callback.notify("Đã xử lí")
         callback.complete()
    }



}