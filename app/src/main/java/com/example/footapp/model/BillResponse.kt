package com.example.footapp.model

data class BillResponse(
    val id: Int,
    val totalPrice: Int? = 0,
) : java.io.Serializable
