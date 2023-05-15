package com.example.footapp.network

import com.example.footapp.BaseResponse
import com.example.footapp.model.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("login")
    suspend fun login(@Body user: Test): BaseResponse<LoginResponse>

    @GET("api/home/all")
    suspend fun getItems(): BaseResponse<List<Item>>

    @POST("api/bill")
    suspend fun makeBill(@Body request: List<ItemBillRequest>): BaseResponse<BillResponse>
}
