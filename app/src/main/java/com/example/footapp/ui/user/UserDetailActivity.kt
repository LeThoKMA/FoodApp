package com.example.footapp.ui.user

import android.os.Build
import android.view.View
import android.widget.Toast
import com.example.footapp.R
import com.example.footapp.interface1.UserInterface
import com.example.footapp.databinding.ActivityUserDetailBinding
import com.example.footapp.model.User
import com.example.footapp.presenter.UserPresenter
import com.example.footapp.ui.BaseActivity

class UserDetailActivity : BaseActivity<ActivityUserDetailBinding>(), View.OnClickListener,
    UserInterface {
    var user = User()
    lateinit var userPresenter: UserPresenter
    override fun getContentLayout(): Int {
        return R.layout.activity_user_detail
    }

    override fun initView() {
        userPresenter = UserPresenter(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            user = intent.getSerializableExtra("user", User::class.java) ?: User()
        } else {
            user = intent.getSerializableExtra("user") as User
        }
      binding.edtName.isEnabled=false
        binding.edtSalary.isEnabled=false

        binding.edtName.setText(user.name)
        binding.edtSalary.setText(user.salary.toString())


    }

    override fun initListener() {
        binding.tvRegister.setOnClickListener(this)
        binding.imgEdit.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        if(p0?.id?.equals(binding.imgEdit.id)==true)
        {
            binding.edtName.isEnabled=true
            binding.edtSalary.isEnabled=true
            binding.tvRegister.visibility=View.VISIBLE

        }
        if(p0?.id?.equals(binding.tvRegister.id)==true)
        {

            updateUser()
        }
    }

    private fun updateUser() {
        user.name=binding.edtName.text.toString()
        user.salary=binding.edtSalary.text.toString().toInt()
          userPresenter.updateUser(user)

    }


    override fun deleteUser(position: Int) {

    }


    override fun notify(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()

    }

    override fun complete() {
        finish()
    }


}