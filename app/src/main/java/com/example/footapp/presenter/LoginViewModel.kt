package com.example.footapp.presenter

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.footapp.BaseViewModel
import com.example.footapp.MyPreference
import com.example.footapp.model.Test
import com.example.footapp.model.User
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
        signIn()
    }

    fun signIn() {
        viewModelScope.launch {
            flow { emit(apiService.login(Test("u1", "pw1"))) }.flowOn(Dispatchers.IO)
                .onStart { onRetrievePostListStart() }
                .onCompletion { onRetrievePostListFinish() }
                .catch { println(it.message) }
                .collect {
                    println(it.toString())
                    if (it.data?.token?.isNotBlank() == true) {
                        _doLogin.postValue(true)
                    }
                }
        }
    }

    fun validUser(username: String, password: String, users: List<User>) {
        var flag = false
        for (user in users) {
            if (username == user.name) {
                if (password == user.password) {
                    myPreference.saveUser(
                        user.id.toString(),
                        username.toString(),
                        user.password.toString(),
                        user.salary.toString(),
                        user.admin ?: 0,
                    )
                    //  callBack.loginSuccess()
                } else {
                    // callBack.messageLogin("Mật khẩu không chính xác")
                }
                flag = true
                break
            }
        }
        if (!flag) {
            // callBack.messageLogin("Tài khoản không tồn tại")
        }
    }
}
