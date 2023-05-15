package com.example.footapp.presenter

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.footapp.BaseViewModel
import com.example.footapp.MyPreference
import com.example.footapp.di.NetworkModule
import com.example.footapp.model.Test
import com.example.footapp.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel(val context: Context) : BaseViewModel() {
    @Inject
    lateinit var apiService: ApiService
    private val _doLogin = MutableLiveData<Boolean>()
    val doLogin: LiveData<Boolean> = _doLogin
    var myPreference: MyPreference

    init {

        myPreference = MyPreference().getInstance(context)!!
        signIn("u1","pw1")
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            flow { emit(apiService.login(Test(email, password))) }.flowOn(Dispatchers.IO)
                .onStart { onRetrievePostListStart() }
                .onCompletion { onRetrievePostListFinish() }
                .catch { Toast.makeText(context, it.message, Toast.LENGTH_LONG).show() }
                .collect {
                    println(it.toString())
                    if (it.data?.token?.isNotBlank() == true) {
                        NetworkModule.mToken = it.data.token
                        _doLogin.postValue(true)
                    }
                }
        }
    }
}
