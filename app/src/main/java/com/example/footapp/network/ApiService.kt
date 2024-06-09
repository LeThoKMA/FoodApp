package com.example.footapp.network

import com.example.footapp.Request.ChangePassRequest
import com.example.footapp.Request.ConfirmBillRequest
import com.example.footapp.Request.RefreshTokenRequest
import com.example.footapp.Request.RegisterRequest
import com.example.footapp.Response.BaseResponse
import com.example.footapp.Response.BaseResponseNoBody
import com.example.footapp.Response.BillDetailResponse
import com.example.footapp.Response.BillResponse
import com.example.footapp.Response.BillsResponse
import com.example.footapp.Response.CategoryResponse
import com.example.footapp.Response.LoginResponse
import com.example.footapp.Response.QrResponse
import com.example.footapp.Response.RefreshTokenResponse
import com.example.footapp.Response.StatisticResponse
import com.example.footapp.model.Item
import com.example.footapp.model.ItemBillRequest
import com.example.footapp.model.ItemStatistic
import com.example.footapp.model.LoginModel
import com.example.footapp.model.StaffData
import com.example.footapp.model.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body user: LoginModel): BaseResponse<LoginResponse>

    @POST("auth/refresh-token")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): BaseResponse<RefreshTokenResponse>

    @POST("auth/register/store")
    suspend fun registerStore(
        @Body request: RegisterRequest,
        @Query("isStaff") isStaff: Boolean = true
    ): BaseResponseNoBody


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
    ): BaseResponse<BillsResponse>

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

    @GET("tracking/{time}")
    suspend fun getStatisticStaff(@Path("time") time: String): BaseResponse<List<StaffData>>

    @GET("tracking")
    suspend fun getTimeForStatisticUser(): BaseResponse<List<String>>

    @GET("category")
    suspend fun getCategory(): BaseResponse<List<CategoryResponse>>

    @GET("home/{id}")
    suspend fun getProductByType(@Path("id") id: Int): BaseResponse<List<Item>>

    @POST("bill/add/offline/{id}")
    suspend fun postBillOffline(
        @Body request: List<ItemBillRequest>,
        @Path("id") id: Int
    ): BaseResponseNoBody

    @GET("bill/qr/default")
    suspend fun getQrDefault(): BaseResponse<QrResponse>
}
