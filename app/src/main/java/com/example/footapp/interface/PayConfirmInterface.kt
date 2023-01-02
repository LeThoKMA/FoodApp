package com.example.footapp.`interface`

import com.example.footapp.model.Bill
import com.example.footapp.model.DetailItemChoose
import com.example.footapp.model.Item

interface PayConfirmInterface {
    fun complete(message:String,flag:Boolean)
}