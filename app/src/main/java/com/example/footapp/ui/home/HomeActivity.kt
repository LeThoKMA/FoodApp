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
import com.example.footapp.ui.ManageActivity
import com.example.footapp.ui.login.SignInActivity


class HomeActivity : BaseActivity<ActivityHomeBinding>()
{
    var listTable:ArrayList<Table> = arrayListOf()
     lateinit  var adapter:HomeAdapter

    override fun getContentLayout(): Int {
        return R.layout.activity_home
    }

    override fun initView() {

     for(i in 0 until 20)
     {
         var item=Table(i,false)
         listTable.add(item)
     }
        adapter= HomeAdapter(listTable)
        binding.rcView.layoutManager=GridLayoutManager(this,2)
        binding.rcView.adapter=adapter

        var user= MyPreference().getInstance(this)?.getUser()
        if(user?.admin==1)
        {
            binding.imgEdit.visibility=View.VISIBLE
        }
        else
        {
            binding.imgLogout.visibility=View.VISIBLE
        }

        if(intent.hasExtra("pos_table"))
        {
            adapter.updateView(intent.getIntExtra("pos_table",0))
        }

    }


    override fun initListener() {
       binding.imgEdit.setOnClickListener {
           startActivity(Intent(this,ManageActivity::class.java))
       }
        binding.imgLogout.setOnClickListener {
             showDialog()
        }
    }
    fun showDialog()
    {
        var dialog= ConfirmDialog(object : ConfirmDialog.CallBack {
            override fun accept() {
                MyPreference().getInstance(this@HomeActivity)?.logout()
                val intent = Intent(applicationContext, SignInActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }

        })
        dialog.show(supportFragmentManager,"")
    }

    override fun onDestroy() {

        super.onDestroy()
    }

}