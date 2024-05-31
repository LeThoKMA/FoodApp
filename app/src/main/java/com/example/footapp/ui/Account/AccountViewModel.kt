package com.example.footapp.ui.Account

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.footapp.MyPreference
import com.example.footapp.Request.ChangePassRequest
import com.example.footapp.base.BaseViewModel
import com.example.footapp.di.App
import com.example.footapp.network.ApiService
import com.example.footapp.utils.toast
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class AccountViewModel() : BaseViewModel() {
    @Inject
    lateinit var apiService: ApiService
    val logout = MutableLiveData<Boolean>()
    val message = MutableLiveData<String>()

    fun logout() {
        viewModelScope.launch {
            flow { emit(apiService.logout()) }
                .catch { handleApiError(it) }
                .collect {
                    MyPreference().getInstance(App.appComponent.getContext())?.logout()
                    logout.postValue(true)
                }
        }
    }

    fun changePass(old: String, new: String, repeat: String) {
        viewModelScope.launch {
            if (new != repeat) {
                App.appComponent.getContext().toast("Mật khẩu không trùng khớp")
                return@launch
            }
            if (new.length < 6) {
                App.appComponent.getContext().toast("Mật khẩu tối thiểu 6 kí tự")
                return@launch
            }
            flow {
                emit(
                    apiService.changePass(
                        ChangePassRequest(
                            old,
                            new,
                        ),
                    ),
                )
            }.onStart { onRetrievePostListStart() }
                .onCompletion { onRetrievePostListFinish() }
                .catch { handleApiError(it) }
                .collect {
                    it.message?.let { it1 -> message.postValue(it1) }
                }
        }
    }
}
