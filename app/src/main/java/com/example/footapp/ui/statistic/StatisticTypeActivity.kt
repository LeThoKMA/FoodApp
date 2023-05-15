package com.example.footapp.ui.statistic

import android.content.Intent
import com.example.footapp.R
import com.example.footapp.databinding.ActivityStatisticTypeBinding
import com.example.footapp.presenter.OrderViewModel
import com.example.footapp.ui.BaseActivity

class StatisticTypeActivity : BaseActivity<ActivityStatisticTypeBinding, OrderViewModel>() {
    override fun getContentLayout(): Int {
        return R.layout.activity_statistic_type
    }

    override fun initView() {
        setColorForStatusBar(R.color.colorPrimary)
        setLightIconStatusBar(false)
    }

    override fun initListener() {
        binding.tvMonth.setOnClickListener {
            startActivity(Intent(this, StatisticByMonthActivity::class.java))
        }
        binding.tvYear.setOnClickListener {
            startActivity(Intent(this, StatisticByYearActivity::class.java))
        }
        binding.imvBack.setOnClickListener { finish() }
    }

    override fun observerData() {
        TODO("Not yet implemented")
    }

    override fun initViewModel() {
        TODO("Not yet implemented")
    }
}
