package com.example.footapp.ui.Account

import android.content.DialogInterface
import android.content.Intent
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.footapp.MyPreference
import com.example.footapp.R
import com.example.footapp.ViewModelFactory
import com.example.footapp.base.BaseFragment
import com.example.footapp.databinding.ActivityManageBinding
import com.example.footapp.ui.login.LoginActivity

class AccountFragment : BaseFragment<ActivityManageBinding, AccountViewModel>() {

    override fun getContentLayout(): Int {
        return R.layout.activity_manage
    }

    override fun initView() {
        val binding = binding!!
        binding.let { paddingStatusBar(it.root) }
        val user = MyPreference().getInstance(requireContext())?.getUser()
        if (user?.role == 1) {
            binding.tvManageUser?.visibility = View.GONE
            binding.tvStatistic?.visibility = View.GONE
            binding.tvItem?.visibility = View.GONE
            binding.tvBills?.visibility = View.GONE
        }
    }

    override fun initListener() {
        binding?.tvLogout?.setOnClickListener {
            showDialog()
        }
        binding?.tvChangePass?.setOnClickListener {
            startActivity(
                Intent(
                    requireContext(),
                    ChangePassActivity::class.java,
                ),
            )
        }
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(),
        )[AccountViewModel::class.java]
    }

    override fun observerLiveData() {
        viewModel.logout.observe(viewLifecycleOwner) {
            if (it) {
                val intent = Intent(this.requireContext(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        }
    }

    fun showDialog() {
        val alertDialog: AlertDialog = this.let {
            val builder = AlertDialog.Builder(requireContext())
            builder.apply {
                setMessage("Bạn có muốn đăng xuất")
                setPositiveButton(
                    "Đồng ý",
                    DialogInterface.OnClickListener { dialog, id ->
                        viewModel.logout()
                        dialog.dismiss()
                    },
                )
                setNegativeButton(
                    "Hủy",
                    DialogInterface.OnClickListener { dialog, id ->
                        dialog.dismiss()
                    },
                )
            }
            builder.create()
        }
        alertDialog.show()
    }
}
