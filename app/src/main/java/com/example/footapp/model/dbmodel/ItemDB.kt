package com.example.footapp.model.dbmodel

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity(
    tableName = "ItemDB",
    foreignKeys = [
        ForeignKey(
            entity = TypeDB::class,
            parentColumns = ["id"],
            childColumns = ["typeId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],

    indices = [Index(value = ["typeId"])]
)
@Parcelize
data class ItemDB(
    @PrimaryKey
    val id: Int? = 0,
    val name: String? = "",
    val price: Int? = 0,
    val amount: Int? = 0,
    val imgUrl: String? = null,
    val typeId: Int = 0
) : Parcelable
