package com.example.footapp

data class BaseResponse<T>(
    val message: String? = "",
    val data: T?
)
