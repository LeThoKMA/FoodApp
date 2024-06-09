package com.example.footapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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

class ViewModelFactory() : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel() as T
        }
        if (modelClass.isAssignableFrom(OrderViewModel::class.java)) {
            return OrderViewModel() as T
        }
        if (modelClass.isAssignableFrom(CustomerViewModel::class.java)) {
            return CustomerViewModel() as T
        }
        if (modelClass.isAssignableFrom(PayConfirmViewModel::class.java)) {
            return PayConfirmViewModel() as T
        }
        if (modelClass.isAssignableFrom(OrderListViewModel::class.java)) {
            return OrderListViewModel() as T
        }
        if (modelClass.isAssignableFrom(AccountViewModel::class.java)) {
            return AccountViewModel() as T
        }
        if (modelClass.isAssignableFrom(StatisticViewModel::class.java)) {
            return StatisticViewModel() as T
        }

        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel() as T
        }

        if (modelClass.isAssignableFrom(OrderWhenNetworkErrorViewModel::class.java)) {
            return OrderWhenNetworkErrorViewModel() as T
        }

        if (modelClass.isAssignableFrom(OfflineConfirmViewModel::class.java)) {
            return OfflineConfirmViewModel() as T
        }

        if (modelClass.isAssignableFrom(AddUserViewModel::class.java)) {
            return AddUserViewModel() as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
