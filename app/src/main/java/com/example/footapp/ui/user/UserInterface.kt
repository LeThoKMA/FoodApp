package com.example.footapp.ui.user

import com.example.footapp.model.User

interface UserInterface {
    fun deleteUser(position:Int)
    fun notify(message:String)
    fun complete()
}