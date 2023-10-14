package com.example.footapp.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("email")
    var email: String? = "",
    @SerializedName("fullname")
    var fullname: String? = "",
    @SerializedName("id")
    var id: Int? = 0,
    @SerializedName("role")
    var role: Int? = 0,
    @SerializedName("username")
    var username: String? = "",
) : java.io.Serializable
