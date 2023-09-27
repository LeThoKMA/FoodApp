package com.example.footapp.Response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("access-token")
    val token: String,
    val role: Int,
)
