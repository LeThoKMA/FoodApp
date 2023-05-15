package com.example.footapp.ui.manage

import android.content.Intent
import android.view.View
import com.example.footapp.MyPreference
import com.example.footapp.R
import com.example.footapp.databinding.ActivityManageBinding
import com.example.footapp.presenter.OrderViewModel
import com.example.footapp.ui.BaseActivity
import com.example.footapp.ui.history.HistoryActivity
import com.example.footapp.ui.home.ConfirmDialog
import com.example.footapp.ui.item.ItemActivity
import com.example.footapp.ui.login.SignInActivity
import com.example.footapp.ui.statistic.StatisticTypeActivity
import com.example.footapp.ui.user.ManageUserActivity

class ManageActivity : BaseActivity<ActivityManageBinding, OrderViewModel>() {

    fun showDialog() {
        var dialog = ConfirmDialog(object : ConfirmDialog.CallBack {
            override fun accept() {
                MyPreference().getInstance(this@ManageActivity)?.logout()
                val intent = Intent(applicationContext, SignInActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
        })
        dialog.show(supportFragmentManager, "")
    }

    override fun getContentLayout(): Int {
        return R.layout.activity_manage
    }

    override fun initView() {
        setColorForStatusBar(R.color.colorPrimary)
        setLightIconStatusBar(false)
        var user = MyPreference().getInstance(this)?.getUser()
        if (user?.admin == 0) {
            binding.tvManageUser.visibility = View.GONE
            binding.tvStatistic.visibility = View.GONE
            binding.tvItem.visibility = View.GONE
            binding.tvBills.visibility = View.GONE
        }
    }

    override fun initListener() {
        binding.imvBack.setOnClickListener { finish() }
        binding.tvManageUser.setOnClickListener {
            startActivity(Intent(this, ManageUserActivity::class.java))
        }

        binding.tvItem.setOnClickListener {
            startActivity(Intent(this, ItemActivity::class.java))
        }
        binding.tvStatistic.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    StatisticTypeActivity::class.java,
                ),
            )
        }
        binding.tvBills.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    HistoryActivity::class.java,
                ),
            )
        }
        binding.tvLogout.setOnClickListener {
            showDialog()
        }
        binding.tvChangePass.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    ChangePassActivity::class.java,
                ),
            )
        }
    }

    override fun observerData() {
    }

    override fun initViewModel() {
    }
}
