package com.example.footapp.Request

data class ChangePassRequest(
    val oldPassword: String,
    val newPassword: String,
)
