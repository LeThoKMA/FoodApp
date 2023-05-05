package com.example.footapp.ui.login

import androidx.lifecycle.ViewModelProvider
import com.example.footapp.R
import com.example.footapp.ViewModelFactory
import com.example.footapp.databinding.ActivitySignInBinding
import com.example.footapp.presenter.LoginViewModel
import com.example.footapp.ui.BaseActivity

class SignInActivity : BaseActivity<ActivitySignInBinding, LoginViewModel>() {

    override fun initListener() {
        binding.btnSignIn.setOnClickListener {
        }
    }

    override fun getContentLayout(): Int {
        return R.layout.activity_sign_in
    }

    override fun initView() {
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(this, ViewModelFactory(this))[LoginViewModel::class.java]
    }

    override fun observerData() {
        viewModel.doLogin.observe(this) {
        }
    }
}
