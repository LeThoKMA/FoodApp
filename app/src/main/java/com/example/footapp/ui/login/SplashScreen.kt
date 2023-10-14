package com.example.footapp.ui.login

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import com.example.footapp.MainActivity
import com.example.footapp.MyPreference
import com.example.footapp.R
import com.example.footapp.ViewModelFactory
import com.example.footapp.base.BaseActivity
import com.example.footapp.databinding.ActivitySplashScreenBinding
import com.example.footapp.model.User

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
        if (preference?.getUser() == User()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finishAffinity()
        } else {
            viewModel.signIn(
                preference?.getUser()?.username.toString(),
                preference?.getPasswd().toString(),
            )
        }
    }

    override fun initListener() {
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(this, ViewModelFactory(this))[LoginViewModel::class.java]
    }
}
