package com.example.footapp.Response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryResponse(
    val id: Int,
    val name: String,
    var isPicked: Boolean = false
) : Parcelable
