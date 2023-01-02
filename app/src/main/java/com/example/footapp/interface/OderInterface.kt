package com.example.footapp.`interface`

import com.example.footapp.model.Bill
import com.example.footapp.model.DetailItemChoose
import com.example.footapp.model.Item

interface OderInterface {
    fun price(priceItem:Int)
    fun complete(message:String)
    fun confirm(map: HashMap<Int,DetailItemChoose>,totalPrice:Int)
}