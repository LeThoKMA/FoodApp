package com.example.footapp.Response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("access-token")
    val token: String,
    @SerializedName("refresh-token")
    val refreshToken: String,
    val role: Int,
)
