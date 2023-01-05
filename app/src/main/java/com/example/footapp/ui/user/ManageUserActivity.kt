package com.example.footapp.ui.user

import android.content.Intent
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.footapp.R
import com.example.footapp.`interface`.UserInterface
import com.example.footapp.databinding.ActivityManageUserBinding
import com.example.footapp.model.User
import com.example.footapp.presenter.UserPresenter
import com.example.footapp.ui.BaseActivity
import com.example.footapp.utils.TOTAL_USER
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class ManageUserActivity : BaseActivity<ActivityManageUserBinding>(), UserInterface {

    lateinit var adapter: UserAdapter
    lateinit var databaseReference: DatabaseReference
    lateinit var userPresenter: UserPresenter
    var list: ArrayList<User?> = arrayListOf()
    var size = 0

    override fun initView() {
        userPresenter = UserPresenter(this)
        loadingDialog?.show()
        adapter = UserAdapter(list, this, userPresenter)
        binding.rcView.layoutManager = LinearLayoutManager(this)
        binding.rcView.adapter = adapter

        userPresenter.users.observe(this@ManageUserActivity)
        {
            if (it != null) {
                list.clear()
                list.addAll(it)
                adapter.notifyDataSetChanged()
                loadingDialog?.dismiss()
            }
        }


    }

    override fun getContentLayout(): Int {
        return R.layout.activity_manage_user
    }

    override fun initListener() {
        binding.imvBack.setOnClickListener { finish() }
        binding.imgPlus.setOnClickListener {
            var intent = Intent(this, AddUserActivity::class.java)
            intent.putExtra(TOTAL_USER, size)
            startActivity(intent)
        }
    }

    override fun deleteUser(position: Int) {
        adapter.deleteUser(position)
    }


    override fun notify(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun complete() {
        finish()
    }


}