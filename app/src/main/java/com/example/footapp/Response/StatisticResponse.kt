package com.example.footapp.Response

import com.example.footapp.model.ItemStatistic
import com.google.gson.annotations.SerializedName

data class StatisticResponse(
    @SerializedName("maxValue")
    val maxValue: Int = 0,
    val values: List<ItemStatistic> = listOf(),
    val total: Int = 0
)
