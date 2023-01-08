package com.example.footapp.model

import com.google.gson.annotations.SerializedName

data class ItemInRecycler( @SerializedName("id")
                           var id:Int?=0,
                           @SerializedName("price")
                           var price:Int?=0,
                           @SerializedName("amount")
                           var count:Int?=0,
                           var amount:Int?=0

                          )
