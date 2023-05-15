package com.example.footapp.presenter

import android.content.Context
import android.util.Log
import com.example.footapp.BaseViewModel
import com.example.footapp.repository.CustomerRepository
import javax.inject.Inject

class CustomerViewModel(val context: Context) : BaseViewModel() {
    @Inject
    lateinit var repository: CustomerRepository
}
