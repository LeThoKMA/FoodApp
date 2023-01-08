package com.example.footapp.model

import com.google.firebase.database.IgnoreExtraProperties
import com.google.gson.annotations.SerializedName


@IgnoreExtraProperties

data class Item(
    @SerializedName("id")
    var id:Int?=0,
    @SerializedName("name")
    var name:String?="",
    @SerializedName("price")
    var price:Int?=0,
    @SerializedName("amount")
    var amount:Int?=0,
    @SerializedName("imgUrl")
    var imgUrl:String ? ="",
    var type:Int? = 0,

):java.io.Serializable
