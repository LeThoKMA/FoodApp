package com.example.footapp.ui.user

import android.view.View
import android.widget.Toast
import com.example.footapp.R
import com.example.footapp.`interface`.UserInterface
import com.example.footapp.databinding.ActivityAddUserBinding
import com.example.footapp.model.User
import com.example.footapp.presenter.UserPresenter
import com.example.footapp.ui.BaseActivity
import com.example.footapp.utils.TOTAL_USER

class AddUserActivity : BaseActivity<ActivityAddUserBinding>(), View.OnClickListener,
    UserInterface {
    var user=User()
    lateinit var userPresenter: UserPresenter
    override fun getContentLayout(): Int {
        return R.layout.activity_add_user
    }

    override fun initView() {
        userPresenter = UserPresenter(this)


    }

    override fun initListener() {
        binding.tvRegister.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        if (p0?.id?.equals(binding.tvRegister.id) == true) {
            addUser()
        }
    }

    private fun addUser() {
        var id = intent.getIntExtra(TOTAL_USER, -1)

        var user = User(
            id,
            binding.edtName.text.toString(),
            binding.edtPasswd.text.toString(),
            binding.edtSalary.text.toString().toInt()
        )
        userPresenter.addUser(
            user,
            binding.edtPasswd.text.toString(),
            binding.edtConfirmPasswd.text.toString()
        )

    }


    override fun deleteUser(position: Int) {

    }



    override fun notify(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show()

    }

    override fun complete() {
        finish()
    }


}