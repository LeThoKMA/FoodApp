package com.example.footapp.ui.login

import android.app.ActivityOptions
import android.content.Intent
import android.hardware.display.DisplayManager
import androidx.lifecycle.ViewModelProvider
import com.example.footapp.MainActivity
import com.example.footapp.R
import com.example.footapp.ViewModelFactory
import com.example.footapp.databinding.ActivitySignInBinding
import com.example.footapp.base.BaseActivity
import com.example.footapp.ui.customer.CustomerActivity

class SignInActivity : BaseActivity<ActivitySignInBinding, LoginViewModel>() {

    override fun initListener() {
        binding.btnSignIn.setOnClickListener {
            viewModel.signIn(
                binding.edtName.text.toString(),
                binding.edtPasswd.text.toString(),
            )
        }
    }

    override fun getContentLayout(): Int {
        return R.layout.activity_sign_in
    }

    override fun initView() {
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(this, ViewModelFactory())[LoginViewModel::class.java]
    }

    override fun observerData() {
        viewModel.doLogin.observe(this) {
            if (it) {
                startActivity(Intent(this, MainActivity::class.java))
                finishAffinity()
            }
        }
    }
}
