package com.example.footapp.network

import com.example.footapp.Request.ChangePassRequest
import com.example.footapp.Request.ConfirmBillRequest
import com.example.footapp.Response.*
import com.example.footapp.model.*
import retrofit2.http.*

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body user: Test): BaseResponse<LoginResponse>

    @GET("home/all")
    suspend fun getItems(): BaseResponse<List<Item>>

    @POST("bill")
    suspend fun makeBill(@Body request: List<ItemBillRequest>): BaseResponse<BillResponse>

    @GET("user")
    suspend fun fetchUserInfo(): BaseResponse<User>

    @POST("bill/status")
    suspend fun confirmBill(@Body request: ConfirmBillRequest): BaseResponseNoBody

    @GET("bill/all")
    suspend fun getOrderList(
        @Query("page") page: Int,
        @Query("time") time: String?,
        @Query("status") status: Int?,
    ): BaseResponse<List<OrderItem>?>

    @GET("bill/detail/{id}")
    suspend fun getOrderDetail(@Path("id") id: Int): BaseResponse<BillDetailResponse>

    @GET("statistic/year")
    suspend fun getYearToStatistic(): BaseResponse<List<Int>>

    @GET("statistic/year/{year}")
    suspend fun getYearToStatistic(@Path("year") year: Int): BaseResponse<StatisticResponse>

    @GET("statistic/today")
    suspend fun getStatisticInToday(): BaseResponse<ItemStatistic>

    @GET("banner")
    suspend fun getBannerList(): BaseResponse<List<String>>

    @GET("logout")
    suspend fun logout(): BaseResponseNoBody

    @PUT("user/password")
    suspend fun changePass(@Body request: ChangePassRequest): BaseResponseNoBody

    @GET("tracking/{month}/{year}")
    suspend fun getStatisticStaff(@Path("month") month: Int, @Path("year") year: Int): BaseResponse<List<StaffData>>
}
