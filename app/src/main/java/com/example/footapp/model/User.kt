package com.example.footapp.model

import com.google.firebase.database.IgnoreExtraProperties
import com.google.gson.annotations.SerializedName

@IgnoreExtraProperties
data class User(
   @SerializedName("id")
   var id:Int?=0,
   @SerializedName("name")
   var name:String?="",
   @SerializedName("password")
   var password:String?="",
   @SerializedName("salary")
   var salary:Int?=0,

   var admin:Int? = 0,
   var delete:Int= 0

):java.io.Serializable