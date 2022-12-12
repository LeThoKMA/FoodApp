package com.example.footapp.model

data class Bill(
    var idUser:Int?,
    var idsItem:ArrayList<Int>? = arrayListOf(),
    var totalPrice:Int?

)
