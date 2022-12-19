package com.example.footapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.footapp.R
import com.example.footapp.databinding.ActivityManageBinding
import com.example.footapp.ui.user.ManageUserActivity

class ManageActivity : AppCompatActivity() {
    lateinit var binding : ActivityManageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_manage)

        binding.imvBack.setOnClickListener { finish() }
        binding.tvManageUser.setOnClickListener{
          startActivity(Intent(this, ManageUserActivity::class.java))
        }
        binding.tvItem.setOnClickListener{

        }
        binding.tvStatistic.setOnClickListener{

        }
    }


}