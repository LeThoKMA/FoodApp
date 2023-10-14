package com.example.footapp.ui.Account

import androidx.lifecycle.ViewModelProvider
import com.example.footapp.R
import com.example.footapp.ViewModelFactory
import com.example.footapp.base.BaseActivity
import com.example.footapp.databinding.ActivityChangePassBinding
import com.example.footapp.utils.toast

class ChangePassActivity :
    BaseActivity<ActivityChangePassBinding, AccountViewModel>() {
    override fun getContentLayout(): Int {
        return R.layout.activity_change_pass
    }

    override fun initView() {
        setColorForStatusBar(R.color.colorPrimary)
        setLightIconStatusBar(false)
    }

    override fun initListener() {
        binding.tvSave.setOnClickListener {
            viewModel.changePass(
                binding.edtPassOld.text.toString(),
                binding.editPassNew.text.toString(),
                binding.edtPassRepeat.text.toString(),
            )
        }
        binding.imvBack.setOnClickListener { finish() }
    }

    override fun observerData() {
        viewModel.message.observe(this){
            toast(it)
            finish()
        }
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(binding.root.context),
        )[AccountViewModel::class.java]
    }
}
