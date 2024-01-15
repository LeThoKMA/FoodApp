package com.example.footapp.Response

import com.example.footapp.model.OrderItem

data class BillsResponse(
    val totalPage: Int = 0,
    val billResponses: List<OrderItem> = listOf(),
)
