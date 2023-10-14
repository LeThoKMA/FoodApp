package com.example.footapp.ui.user

import android.view.View
import com.example.footapp.R
import com.example.footapp.base.BaseActivity
import com.example.footapp.databinding.ActivityAddUserBinding
import com.example.footapp.model.User
import com.example.footapp.ui.Order.OrderViewModel

class AddUserActivity :
    BaseActivity<ActivityAddUserBinding, OrderViewModel>(),
    View.OnClickListener {
    var user = User()
    override fun getContentLayout(): Int {
        return R.layout.activity_add_user
    }

    override fun initView() {
        setColorForStatusBar(R.color.colorPrimary)
        setLightIconStatusBar(false)
    }

    override fun initListener() {
        binding.tvRegister.setOnClickListener(this)
        binding.imvBack.setOnClickListener { finish() }
    }

    override fun onClick(p0: View?) {
        if (p0?.id?.equals(binding.tvRegister.id) == true) {
            //  addUser()
        }
    }

//    private fun addUser() {
//        var id = intent.getIntExtra(TOTAL_USER, -1)
//
//        var user = User(
//            id,
//            binding.edtName.text.toString(),
//            binding.edtPasswd.text.toString(),
//            binding.edtSalary.text.toString().toInt(),
//        )
//        userPresenter.addUser(
//            user,
//            binding.edtPasswd.text.toString(),
//            binding.edtConfirmPasswd.text.toString(),
//        )
//    }

    override fun observerData() {
        TODO("Not yet implemented")
    }

    override fun initViewModel() {
        TODO("Not yet implemented")
    }
}
