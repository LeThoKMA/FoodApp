package com.example.footapp.model

data class Bill(
    var idBill:Int?,
    var idUser:Int?,
    var idsItem:ArrayList<Int>? = arrayListOf(),
    var totalPrice:Int?,
    var dateTime:String?
)
