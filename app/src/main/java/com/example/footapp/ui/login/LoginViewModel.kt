package com.example.footapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.footapp.MyPreference
import com.example.footapp.Request.RefreshTokenRequest
import com.example.footapp.base.BaseViewModel
import com.example.footapp.di.NetworkModule
import com.example.footapp.model.LoginModel
import com.example.footapp.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.net.ssl.HttpsURLConnection

class LoginViewModel() : BaseViewModel() {
    @Inject
    lateinit var apiService: ApiService
    private val _doLogin = MutableLiveData<Boolean>()
    val doLogin: LiveData<Boolean> = _doLogin

    private var myPreference: MyPreference = MyPreference.getInstance()

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
                        myPreference.saveRefreshToken(it.data.refreshToken)
                        myPreference.saveToken(it.data.token)
                        fetchUserInfo()
                    }
                }
        }
    }

    fun fetchUserInfo() {
        viewModelScope.launch {
            flow { emit(apiService.fetchUserInfo()) }.flowOn(Dispatchers.IO)
                .onStart { onRetrievePostListStart() }
                .onCompletion { onRetrievePostListFinish() }
                .catch {
                    errorAuthorize(
                        it,
                        onErrorAuthorize = { refreshToken() },
                        onEach = { handleApiError(it) },
                    )
                }
                .collect {
                    it.data?.let { it1 -> myPreference.saveUser(it1) }
                    _doLogin.postValue(true)
                }
        }
    }

    private fun refreshToken() {
        viewModelScope.launch {
            flow { emit(apiService.refreshToken(RefreshTokenRequest(myPreference.getRefreshToken()))) }
                .onStart { onRetrievePostListStart() }
                .onCompletion { onRetrievePostListFinish() }
                .catch {
                    errorAuthorize(it, onErrorAuthorize = {
                        _doLogin.postValue(false)
                    }, onEach = { handleApiError(it) })
                }
                .collect {
                    it.data?.let { it1 ->
                        NetworkModule.mToken = it1.token
                        myPreference.saveToken(it1.token)
                        fetchUserInfo()
                    }
                }
        }
    }

    private fun errorAuthorize(err: Throwable?, onErrorAuthorize: () -> Unit, onEach: () -> Unit) {
        if (err is retrofit2.HttpException) {
            if (err.code() == HttpsURLConnection.HTTP_UNAUTHORIZED) {
                onErrorAuthorize.invoke()
            } else {
                onEach.invoke()
            }
        }
    }

    fun haveToken(): Boolean = myPreference.getAccessToken()?.isNotBlank() == true
}
