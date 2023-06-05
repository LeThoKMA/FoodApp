package com.example.footapp.Response

data class BaseResponse<T>(
    val message: String? = "",
    val data: T?
)
