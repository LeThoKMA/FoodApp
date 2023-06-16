package com.example.footapp

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.footapp.ui.pay.PayConfirmViewModel
import com.example.footapp.ui.customer.CustomerViewModel
import com.example.footapp.ui.login.LoginViewModel
import com.example.footapp.ui.Order.OrderViewModel
import com.example.footapp.ui.manage.AccountViewModel
import com.example.footapp.ui.orderlist.OrderListViewModel
import com.example.footapp.ui.statistic.StatisticViewModel

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(context) as T
        }
        if (modelClass.isAssignableFrom(OrderViewModel::class.java)) {
            return OrderViewModel(context) as T
        }
        if (modelClass.isAssignableFrom(CustomerViewModel::class.java)) {
            return CustomerViewModel(context) as T
        }
        if (modelClass.isAssignableFrom(PayConfirmViewModel::class.java)) {
            return PayConfirmViewModel(context) as T
        }
        if (modelClass.isAssignableFrom(OrderListViewModel::class.java)) {
            return OrderListViewModel(context) as T
        }
        if (modelClass.isAssignableFrom(AccountViewModel::class.java)) {
            return AccountViewModel(context) as T
        }
        if (modelClass.isAssignableFrom(StatisticViewModel::class.java)) {
            return StatisticViewModel(context) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
