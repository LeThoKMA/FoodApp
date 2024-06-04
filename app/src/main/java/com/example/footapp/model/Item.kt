package com.example.footapp.model

import android.os.Parcelable
import com.example.footapp.Response.CategoryResponse
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class Item(
    @SerializedName("id")
    var id: Int? = 0,
    @SerializedName("name")
    var name: String? = "",
    @SerializedName("unitPrice")
    var price: Int? = 0,
    @SerializedName("quantity")
    var amount: Int? = 0,
    @SerializedName("imageLinks")
    var imgUrl: List<String>? = listOf(),
    val category: CategoryResponse? = null
) : java.io.Serializable, Parcelable
