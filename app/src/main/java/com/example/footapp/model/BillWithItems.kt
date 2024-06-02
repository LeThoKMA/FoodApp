package com.example.footapp.model

import androidx.room.Embedded
import androidx.room.Relation

data class BillWithItems(
    @Embedded val bill: Bill,
    @Relation(
        parentColumn = "billId",
        entityColumn = "billOwnerId",
    )
    val items: List<ItemBill>,
)
