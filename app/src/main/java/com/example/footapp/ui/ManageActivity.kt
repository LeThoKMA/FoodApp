package com.example.footapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.footapp.MyPreference
import com.example.footapp.R
import com.example.footapp.databinding.ActivityManageBinding
import com.example.footapp.ui.home.ConfirmDialog
import com.example.footapp.ui.statistic.StatisticTypeActivity
import com.example.footapp.ui.item.ItemActivity
import com.example.footapp.ui.login.SignInActivity
import com.example.footapp.ui.user.ManageUserActivity

class ManageActivity : AppCompatActivity() {
    lateinit var binding: ActivityManageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_manage)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_manage)
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
                    StatisticTypeActivity::class.java
                )
            )

        }
        binding.tvLogout.setOnClickListener {
        showDialog()

        }
    }
    fun showDialog()
    {
        var dialog= ConfirmDialog(object : ConfirmDialog.CallBack {
            override fun accept() {
                MyPreference().getInstance(this@ManageActivity)?.logout()
                val intent = Intent(applicationContext, SignInActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }

        })
        dialog.show(supportFragmentManager,"")
    }


}