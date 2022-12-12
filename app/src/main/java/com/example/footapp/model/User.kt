package com.example.footapp.model

data class User(
   var id:Int,
   var name:String,
   var salary:Int,
   var isAdmin:Boolean?=false
)