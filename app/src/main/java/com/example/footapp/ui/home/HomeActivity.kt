package com.example.footapp.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.example.footapp.R
import com.example.footapp.databinding.ActivityHomeBinding
import com.example.footapp.ui.BaseActivity
import com.example.footapp.ui.MyPreference

class HomeActivity : BaseActivity<ActivityHomeBinding>()
{
    var listTable:ArrayList<Int> = arrayListOf()
     lateinit  var adapter:HomeAdapter
    override fun getContentLayout(): Int {
        return R.layout.activity_home
    }

    override fun initView() {
     for(i in 0 until 20)
     {
         listTable.add(i)
     }
        adapter= HomeAdapter(listTable)
        binding.rcView.layoutManager=GridLayoutManager(this,2)
        binding.rcView.adapter=adapter

        var user=MyPreference().getInstance(this)?.getUser()
        if(user?.isAdmin==true)
        {
            binding.imgEdit.visibility=View.VISIBLE
        }

    }


    override fun initListener() {

    }

}