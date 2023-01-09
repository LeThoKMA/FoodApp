package com.example.footapp.ui.home

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.example.footapp.R
import com.example.footapp.databinding.ActivityHomeBinding
import com.example.footapp.ui.BaseActivity
import com.example.footapp.MyPreference
import com.example.footapp.model.Table
import com.example.footapp.ui.manage.ManageActivity
import com.example.footapp.ui.login.SignInActivity
import com.example.footapp.utils.POS_BACK
import com.example.footapp.utils.TABLE_ACTION


class HomeActivity : BaseActivity<ActivityHomeBinding>() {
    var listTable: ArrayList<Table> = arrayListOf()
    lateinit var adapter: HomeAdapter
    var broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            if (p1?.hasExtra(POS_BACK) == true) {
                p1?.getIntExtra(POS_BACK, 0)?.let { adapter.updateView(it) }
            }
        }

    }

    override fun getContentLayout(): Int {
        return R.layout.activity_home
    }

    override fun initView() {

        setColorForStatusBar(R.color.colorPrimary)
        setLightIconStatusBar(false)
        var intentFilter = IntentFilter(TABLE_ACTION)
        registerReceiver(broadcastReceiver, intentFilter)
        for (i in 0 until 20) {
            var item = Table(i, false)
            listTable.add(item)
        }
        adapter = HomeAdapter(listTable)
        binding.rcView.layoutManager = GridLayoutManager(this, 2)
        binding.rcView.adapter = adapter


    }


    override fun initListener() {
        binding.imgEdit.setOnClickListener {
            startActivity(Intent(this, ManageActivity::class.java))
        }

    }


    override fun onDestroy() {
        unregisterReceiver(broadcastReceiver)
        super.onDestroy()
    }

}