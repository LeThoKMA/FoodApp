package com.example.footapp.ui.login

import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.ViewModelProvider
import com.example.footapp.MainActivity
import com.example.footapp.MyPreference
import com.example.footapp.R
import com.example.footapp.ViewModelFactory
import com.example.footapp.base.BaseActivity
import com.example.footapp.databinding.ActivitySplashScreenBinding
import com.example.footapp.model.User
import com.example.footapp.ui.Order.offline.OrderWhenNetworkErrorActivity

class SplashScreen : BaseActivity<ActivitySplashScreenBinding, LoginViewModel>() {
    override fun observerData() {
        viewModel.doLogin.observe(this) {
            if (it) {
                startActivity(Intent(this, MainActivity::class.java))
                finishAffinity()
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
                finishAffinity()
            }
        }
    }

    override fun getContentLayout(): Int {
        return R.layout.activity_splash_screen
    }

    override fun initView() {
        val preference = MyPreference().getInstance(this)
        if (checkInitialNetworkStatus()) {
            if (preference?.getUser() == User()) {
                startActivity(Intent(this, LoginActivity::class.java))
                finishAffinity()
            } else {
                viewModel.signIn(
                    preference?.getUser()?.username.toString(),
                    preference?.getPasswd().toString(),
                )
            }
        } else {
            startActivity(Intent(this, OrderWhenNetworkErrorActivity::class.java))
            finishAffinity()
        }
    }

    override fun initListener() {
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(this, ViewModelFactory())[LoginViewModel::class.java]
    }

    private fun checkInitialNetworkStatus(): Boolean {
        val network = applicationContext.getSystemService(
            ConnectivityManager::class.java
        ).activeNetwork
        val networkCapabilities = applicationContext.getSystemService(
            ConnectivityManager::class.java
        ).getNetworkCapabilities(network)
        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }
}
