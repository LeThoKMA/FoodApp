package com.example.footapp.interface1

import com.example.footapp.model.DetailItemChoose

interface OderInterface {
    fun price(priceItem:Int)
    fun complete(message:String)
    fun confirm(map: HashMap<Int,DetailItemChoose>,totalPrice:Int)

}