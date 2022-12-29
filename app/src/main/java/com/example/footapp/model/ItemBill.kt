package com.example.footapp.model

import com.google.firebase.database.IgnoreExtraProperties


@IgnoreExtraProperties

data class ItemBill(
    var id:Int?=0,
    var count:Int?=0
)
