package com.example.footapp.model

import com.google.firebase.database.IgnoreExtraProperties


@IgnoreExtraProperties

data class ItemBill(
    var id:Int?=0,
    var name:String="",
    var count:Int?=0,
    var price:Int?=0
)
