package com.example.footapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.footapp.R
import com.example.footapp.databinding.ActivityManageBinding
<<<<<<< HEAD
import com.example.footapp.ui.statistic.StatisticTypeActivity
=======
import com.example.footapp.ui.item.ItemActivity
>>>>>>> origin/huuduy
import com.example.footapp.ui.user.ManageUserActivity

class ManageActivity : AppCompatActivity() {
    lateinit var binding: ActivityManageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage)
<<<<<<< HEAD
        binding = DataBindingUtil.setContentView(this, R.layout.activity_manage)

=======
        binding=DataBindingUtil.setContentView(this,R.layout.activity_manage)
>>>>>>> origin/huuduy
        binding.imvBack.setOnClickListener { finish() }
        binding.tvManageUser.setOnClickListener {
            startActivity(Intent(this, ManageUserActivity::class.java))
        }
<<<<<<< HEAD
        binding.tvItem.setOnClickListener {

=======
        binding.tvItem.setOnClickListener{
            startActivity(Intent(this, ItemActivity::class.java))
>>>>>>> origin/huuduy
        }
        binding.tvStatistic.setOnClickListener {

                startActivity(
                    Intent(
                        this,
                        StatisticTypeActivity::class.java
                    )
                )

        }
    }


}