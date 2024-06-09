package com.example.footapp.Request

data class RegisterRequest(
    val username: String,
    val password: String,
    val fullname: String,
    val phone: String,
)