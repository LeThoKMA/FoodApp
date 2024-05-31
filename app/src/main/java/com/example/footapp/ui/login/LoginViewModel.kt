package com.example.footapp.ui.login

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.footapp.MyPreference
import com.example.footapp.base.BaseViewModel
import com.example.footapp.di.App
import com.example.footapp.di.NetworkModule
import com.example.footapp.model.LoginModel
import com.example.footapp.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel() : BaseViewModel() {
    @Inject
    lateinit var apiService: ApiService
    private val _doLogin = MutableLiveData<Boolean>()
    val doLogin: LiveData<Boolean> = _doLogin
    private var myPreference: MyPreference = MyPreference().getInstance(App.appComponent.getContext())!!

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            flow { emit(apiService.login(LoginModel(email, password))) }.flowOn(Dispatchers.IO)
                .onStart { onRetrievePostListStart() }
                .onCompletion { onRetrievePostListFinish() }
                .catch {
                    handleApiError(it)
                    _doLogin.postValue(false)
                }
                .collect {
                    println(it.toString())
                    if (it.data?.token?.isNotBlank() == true) {
                        NetworkModule.mToken = it.data.token
                        fetchUserInfo(email, password)
                    }
                }
        }
    }

    private fun fetchUserInfo(email: String, password: String) {
        viewModelScope.launch {
            flow { emit(apiService.fetchUserInfo()) }.flowOn(Dispatchers.IO)
                .onStart { onRetrievePostListStart() }
                .onCompletion { onRetrievePostListFinish() }
                .catch {
                    Toast.makeText(
                        App.appComponent.getContext(),
                        it.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
                .collect {
                    it.data?.let { it1 -> myPreference.saveUser(it1, password) }
                    _doLogin.postValue(true)
                }
        }
    }
}
