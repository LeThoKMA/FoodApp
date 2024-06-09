package com.example.footapp.ui.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.footapp.Request.RegisterRequest
import com.example.footapp.base.BaseViewModel
import com.example.footapp.model.User
import com.example.footapp.network.ApiService
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddUserViewModel() : BaseViewModel() {
    @Inject
    lateinit var apiService: ApiService

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private fun registerStore(registerRequest: RegisterRequest, isStaff: Boolean) {
        viewModelScope.launch {
            flow { emit(apiService.registerStore(registerRequest, isStaff)) }
                .onStart { onRetrievePostListStart() }
                .onCompletion { onRetrievePostListFinish() }
                .catch { handleApiError(it) }
                .collect {
                    _message.postValue(it.message.toString())
                }
        }

    }

    fun register(
        phone: String,
        name: String,
        account: String,
        pass: String,
        passRepeat: String,
        isStaff: Boolean
    ) {
        if (phone.isBlank()) {
            _message.postValue("Số điện thoại không được để trống")
            return
        }

        if (name.isBlank()) {
            _message.postValue("Tên không được để trống")
            return
        }

        if (account.isBlank()) {
            _message.postValue("Tên đăng nhập không được để trống")
            return
        }

        if (pass.isBlank()) {
            _message.postValue("Mật khẩu không được để trống")
            return
        }

        if (passRepeat.isBlank()) {
            _message.postValue("Hãy nhập lại mật khẩu")
            return
        }

        if (pass != passRepeat) {
            _message.postValue("Mật khẩu không trùng khớp")
            return
        }

        val registerRequest = RegisterRequest(username = account, password = pass, phone = phone, fullname =  name)
        registerStore(registerRequest, isStaff)
    }

}