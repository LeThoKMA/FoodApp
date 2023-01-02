package com.example.footapp.`interface`

import com.example.footapp.model.User

interface UserInterface {
    fun deleteUser(position:Int)
    fun notify(message:String)
    fun complete()
}