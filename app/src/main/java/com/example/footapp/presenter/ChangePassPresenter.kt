package com.example.footapp.presenter

import android.content.Context
import com.example.footapp.DAO.DAO
import com.example.footapp.MyPreference
import com.example.footapp.interface1.ChangePassInterface
import com.example.footapp.model.User

class ChangePassPresenter(var context: Context,var callback:ChangePassInterface) {
    private var dao=DAO()
    private var myPreference=MyPreference().getInstance(context)

    fun changePass(oldPass:String,newPass:String,passRepeat:String)
    {
       if(!validate(oldPass,newPass,passRepeat))
       {
           return
       }
        var user=myPreference?.getUser()
        if(oldPass!=user?.password)
        {
            callback.message("Sai mật khẩu")
        }
        else
        {
            if(newPass!=passRepeat)
            {
                callback.message("Mật khẩu không trùng khớp")
            }
            else
            {
                dao.addUser(User(user.id,user.name,newPass,user.salary,user.admin))
                callback.complete()
            }
        }
    }
   private fun validate(oldPass:String,newPass:String,passRepeat:String):Boolean
    {
        if(oldPass.isBlank())
        {
            callback.message("Hãy nhập mật khẩu cũ")
            return false
        }
        if(newPass.isBlank())
        {
            callback.message("Hãy nhập mật khẩu mới")
            return false
        }
        if(passRepeat.isBlank())
        {
            callback.message("Hãy nhập lại mật khẩu mới")
            return false
        }
        return true
    }
}