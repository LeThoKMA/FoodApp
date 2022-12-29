package com.example.footapp.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Bill(
    var idBill:Int?=0,
    var idUser:Int?=0,
    var items:HashMap<String,ArrayList<ItemBill>>? = hashMapOf(),
    var totalPrice:Int?=0,
    var dateTime:String?=""
)
