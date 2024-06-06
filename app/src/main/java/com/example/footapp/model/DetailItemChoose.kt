package com.example.footapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DetailItemChoose(
    var id: Int = 0,
    var name: String? = "",
    var count: Int = 0,
    var price: Int = 0,
    var imgUrl: List<String>? = listOf(),
) : Parcelable {
    val totalPrice get() = count * price
}
