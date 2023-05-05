package com.example.footapp.network

import com.example.footapp.BaseResponse
import com.example.footapp.model.LoginResponse
import com.example.footapp.model.Test
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("login")
    suspend fun login(@Body user: Test): BaseResponse<LoginResponse>
}
