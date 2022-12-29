package com.example.footapp.ui.Oder

import com.example.footapp.model.Bill
import com.example.footapp.model.Item

interface OderInterface {
    fun price(priceItem:Int)
    fun deleteCart(position: Int)
    fun hideKeyboard()
    fun showLog(content: String)
    fun editTextListener(position: Int, data: Item)
    fun minusPrice()
    fun complete(message:String)
    fun confirm(bill: Bill)
}