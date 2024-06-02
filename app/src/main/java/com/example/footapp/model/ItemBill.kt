package com.example.footapp.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "ItemBill",
    foreignKeys = [
        ForeignKey(
            entity = Bill::class,
            parentColumns = ["billId"],
            childColumns = ["billOwnerId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
)
data class ItemBill(
    @PrimaryKey(autoGenerate = true)
    val idRow: Long = 0,
    val productId: Int? = 0,
    val count: Int? = 0,
    val totalPrice: Int? = 0,
    val staffId: Int? = null,
    var billOwnerId: Long = 0,
)
