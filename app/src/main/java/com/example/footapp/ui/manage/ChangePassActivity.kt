package com.example.footapp.ui.manage

import android.widget.Toast
import com.example.footapp.R
import com.example.footapp.base.BaseActivity
import com.example.footapp.databinding.ActivityChangePassBinding
import com.example.footapp.interface1.ChangePassInterface
import com.example.footapp.ui.Order.OrderViewModel

class ChangePassActivity :
    BaseActivity<ActivityChangePassBinding, OrderViewModel>(),
    ChangePassInterface {
    override fun getContentLayout(): Int {
        return R.layout.activity_change_pass
    }

    override fun initView() {
        setColorForStatusBar(R.color.colorPrimary)
        setLightIconStatusBar(false)
    }

    override fun initListener() {
        binding.tvSave.setOnClickListener {
//            presenter.changePass(
//                binding.edtPassOld.text.toString(),
//                binding.editPassNew.text.toString(),
//                binding.edtPassRepeat.text.toString(),
//            )
        }
        binding.imvBack.setOnClickListener { finish() }
    }

    override fun message(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun complete() {
        Toast.makeText(this, "Thành công", Toast.LENGTH_LONG).show()
        finish()
    }

    override fun observerData() {
    }

    override fun initViewModel() {
    }
}
