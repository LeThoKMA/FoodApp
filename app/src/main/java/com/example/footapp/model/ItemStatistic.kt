package com.example.footapp.model

import com.google.gson.annotations.SerializedName

data class ItemStatistic(
    @SerializedName("revenue")
    var revenue: Int = 0,
    @SerializedName("time")
    var time: String? = "",
)
