package com.example.footapp.ui.manage

import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.footapp.MyPreference
import com.example.footapp.R
import com.example.footapp.ViewModelFactory
import com.example.footapp.base.BaseFragment
import com.example.footapp.databinding.ActivityManageBinding
import com.example.footapp.ui.home.ConfirmDialog

class AccountFragment : BaseFragment<ActivityManageBinding, AccountViewModel>() {

    fun showDialog() {
        var dialog = ConfirmDialog(object : ConfirmDialog.CallBack {
            override fun accept() {
                MyPreference().getInstance(binding.root.context)?.logout()
            }
        })
        dialog.show(parentFragmentManager, "")
    }

    override fun getContentLayout(): Int {
        return R.layout.activity_manage
    }

    override fun initView() {
        paddingStatusBar(binding.root)
        val user = MyPreference().getInstance(binding.root.context)?.getUser()
        if (user?.role == true) {
            binding.tvManageUser.visibility = View.GONE
            binding.tvStatistic.visibility = View.GONE
            binding.tvItem.visibility = View.GONE
            binding.tvBills.visibility = View.GONE
        }
    }

    override fun initListener() {
        binding.tvLogout.setOnClickListener {
            showDialog()
        }
        binding.tvChangePass.setOnClickListener {
            startActivity(
                Intent(
                    binding.root.context,
                    ChangePassActivity::class.java,
                ),
            )
        }
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(binding.root.context),
        )[AccountViewModel::class.java]
    }

    override fun observerLiveData() {
    }
}
