package com.example.footapp.ui.user

import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.footapp.R
import com.example.footapp.base.BaseActivity
import com.example.footapp.databinding.ActivityManageUserBinding
import com.example.footapp.model.User
import com.example.footapp.ui.Order.OrderViewModel
import com.example.footapp.utils.TOTAL_USER

class ManageUserActivity :
    BaseActivity<ActivityManageUserBinding, OrderViewModel>(){

    lateinit var adapter: UserAdapter
    var list: ArrayList<User?> = arrayListOf()
    var size = 0

    override fun initView() {
        setColorForStatusBar(R.color.colorPrimary)
        setLightIconStatusBar(false)
        loadingDialog?.show()
        binding.rcView.layoutManager = LinearLayoutManager(this)
        binding.rcView.adapter = adapter
//        userPresenter.updateData.observe(this) {
//            if (it != null) {
//                for (user in list) {
//                    if (it.id == user?.id) {
//                        user?.name = it.name
//                        user?.salary = it.salary
//                        break
//                    }
//                }
//                adapter.notifyDataSetChanged()
//            }
//        }
    }

    override fun getContentLayout(): Int {
        return R.layout.activity_manage_user
    }

    override fun initListener() {
        binding.imvBack.setOnClickListener { finish() }
        binding.imgPlus.setOnClickListener {
            var intent = Intent(this, AddUserFragment::class.java)
            intent.putExtra(TOTAL_USER, size)
            startActivity(intent)
        }
    }
    override fun observerData() {
        TODO("Not yet implemented")
    }

    override fun initViewModel() {
        TODO("Not yet implemented")
    }
}
