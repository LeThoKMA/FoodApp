package com.example.footapp.base

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.footapp.MainViewModel
import com.example.footapp.R
import com.example.footapp.di.App
import com.example.footapp.di.DaggerViewModelInjector
import com.example.footapp.di.DatabaseModule
import com.example.footapp.di.NetworkModule
import com.example.footapp.di.RepositoryModule
import com.example.footapp.di.ViewModelInjector
import com.example.footapp.ui.Account.AccountViewModel
import com.example.footapp.ui.Order.OrderViewModel
import com.example.footapp.ui.Order.offline.OfflineConfirmViewModel
import com.example.footapp.ui.Order.offline.OrderWhenNetworkErrorViewModel
import com.example.footapp.ui.customer.CustomerViewModel
import com.example.footapp.ui.login.LoginViewModel
import com.example.footapp.ui.orderlist.OrderListViewModel
import com.example.footapp.ui.pay.PayConfirmViewModel
import com.example.footapp.ui.statistic.StatisticViewModel
import com.example.footapp.ui.user.AddUserViewModel
import com.example.footapp.worker.UploadWorker
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.File
import java.io.IOException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import javax.net.ssl.HttpsURLConnection

abstract class BaseViewModel : ViewModel() {
    var isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val errorMessage: MutableLiveData<Int?> = MutableLiveData()
    val responseMessage: MutableLiveData<String?> = MutableLiveData()
    private val injector: ViewModelInjector =
        DaggerViewModelInjector.builder().networkModule(NetworkModule).applicationComponent(
            App.appComponent,
        ).databaseModule(DatabaseModule)
            .repositoryComponent(RepositoryModule).build()

    init {
        inject()
    }

    private fun inject() {
        when (this) {
            is LoginViewModel -> injector.inject(this)
            is OrderViewModel -> injector.inject(this)
            is CustomerViewModel -> injector.inject(this)
            is PayConfirmViewModel -> injector.inject(this)
            is OrderListViewModel -> injector.inject(this)
            is AccountViewModel -> injector.inject(this)
            is StatisticViewModel -> injector.inject(this)
            is MainViewModel -> injector.inject(this)
            is OrderWhenNetworkErrorViewModel -> injector.inject(this)
            is OfflineConfirmViewModel -> injector.inject(this)
            is AddUserViewModel -> injector.inject(this)
        }
    }

    protected fun onRetrievePostListStart() {
        isLoading.postValue(true)
        errorMessage.postValue(null)
    }

    protected fun onRetrievePostListFinish() {
        isLoading.postValue(false)
    }

    protected fun handleApiError(error: Throwable?) {
        if (error == null) {
            errorMessage.postValue(R.string.api_default_error)
            return
        }

        if (error is HttpException) {
            Log.w("ERROR", error.message() + error.code())
            when (error.code()) {
                HttpURLConnection.HTTP_BAD_REQUEST -> try {
                    responseMessage.postValue(error.message())
                } catch (e: IOException) {
                    e.printStackTrace()
                    responseMessage.postValue(error.message)
                }

                HttpsURLConnection.HTTP_UNAUTHORIZED -> errorMessage.postValue(R.string.str_authe)
                HttpsURLConnection.HTTP_FORBIDDEN, HttpsURLConnection.HTTP_INTERNAL_ERROR, HttpsURLConnection.HTTP_NOT_FOUND -> responseMessage.postValue(
                    error.message,
                )

                else -> responseMessage.postValue(error.message)
            }
        } else if (error is SocketTimeoutException) {
            errorMessage.postValue(R.string.text_all_has_error_timeout)
        } else if (error is IOException) {
            Log.e("TAG", error.message.toString())
            errorMessage.postValue(R.string.text_all_has_error_network)
        } else {
            if (!TextUtils.isEmpty(error.message)) {
                responseMessage.postValue(error.message)
            } else {
                errorMessage.postValue(R.string.text_all_has_error_please_try)
            }
        }
    }

    fun toMultipartBody(name: String, file: File): MultipartBody.Part {
        val reqFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(name, file.name, reqFile)
    }

    fun toMultipartBody1(name: String, file: File): MultipartBody.Part {
        val reqFile = file.asRequestBody("video/*, image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(name, file.name, reqFile)
    }
}
