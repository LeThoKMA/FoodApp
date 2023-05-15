package com.example.footapp.ui.login

import android.app.ActivityOptions
import android.content.Intent
import android.hardware.display.DisplayManager
import androidx.lifecycle.ViewModelProvider
import com.example.footapp.R
import com.example.footapp.ViewModelFactory
import com.example.footapp.databinding.ActivitySignInBinding
import com.example.footapp.presenter.LoginViewModel
import com.example.footapp.ui.BaseActivity
import com.example.footapp.ui.Order.CartActivity
import com.example.footapp.ui.customer.CustomerActivity

class SignInActivity : BaseActivity<ActivitySignInBinding, LoginViewModel>() {

    override fun initListener() {
        binding.btnSignIn.setOnClickListener {
            viewModel.signIn(
                binding.edtEmailSignIn.text.toString(),
                binding.edtPasswordSignIn.text.toString(),
            )
        }
    }

    override fun getContentLayout(): Int {
        return R.layout.activity_sign_in
    }

    override fun initView() {
        showScreenCustomer()
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(this, ViewModelFactory(this))[LoginViewModel::class.java]
    }

    override fun observerData() {
        viewModel.doLogin.observe(this) {
            if (it) {
                startActivity(Intent(this, CartActivity::class.java))
                finishAffinity()
            }
        }
    }
    private fun showScreenCustomer() {
        val displayManager = getSystemService(DISPLAY_SERVICE) as DisplayManager
        val displays = displayManager.displays
        if (displays.size > 1) {
            // Activity options are used to select the display screen.
            val options = ActivityOptions.makeBasic()

            // Select the display screen that you want to show the second activity
            options.launchDisplayId = displays[1].displayId
            // To display on the second screen that your intent must be set flag to make
            // single task (combine FLAG_ACTIVITY_CLEAR_TOP and FLAG_ACTIVITY_NEW_TASK)
            // or you also set it in the manifest (see more at the manifest file)
            startActivity(
                Intent(this, CustomerActivity::class.java),
                options.toBundle(),
            )
        }
    }
}
