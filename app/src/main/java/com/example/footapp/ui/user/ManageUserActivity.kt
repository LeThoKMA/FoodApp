package com.example.footapp.ui.user

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.footapp.R
import com.example.footapp.databinding.ActivityManageUserBinding
import com.example.footapp.model.User
import com.example.footapp.ui.BaseActivity
import com.example.footapp.utils.TOTAL_USER
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import java.io.StringReader

class ManageUserActivity : BaseActivity<ActivityManageUserBinding>(), UserInterface {

    lateinit var adapter: UserAdapter
    lateinit var databaseReference: DatabaseReference
    lateinit var userPresenter:UserPresenter
    var list: ArrayList<User?> = arrayListOf()
   var size=0

    override fun initView() {
        databaseReference=dao.userReference
        userPresenter= UserPresenter(this)
        loadingDialog?.show()
        adapter= UserAdapter(list,this)
        binding.rcView.layoutManager=LinearLayoutManager(this)
        binding.rcView.adapter=adapter

        val userListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                loadingDialog?.show()
                list.clear()

                dataSnapshot.getValue<List<User>>()?.let { list.addAll(it) }
//                list.addAll(
//                    Gson().fromJson(
//                        dataSnapshot.value.toString(),
//                        object : TypeToken<List<User?>?>() {}.type
//                    )
//
//                )
                size=list.size
                adapter.notifyDataSetChanged()
                loadingDialog?.dismiss()

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        }
        databaseReference.addValueEventListener(userListener)

//        databaseReference.child("users").get()
//            .addOnSuccessListener(OnSuccessListener<DataSnapshot> { dataSnapshot ->
//                list.addAll(
//                    Gson().fromJson(
//                        dataSnapshot.value.toString(),
//                        object : TypeToken<List<User?>?>() {}.type
//                    )
//
//                )
//                size=list.size
//
//                adapter= UserAdapter(list,this)
//
//                binding.rcView.layoutManager=LinearLayoutManager(this)
//                binding.rcView.adapter=adapter
//                loadingDialog?.dismiss()
//            }).addOnFailureListener(OnFailureListener { Log.e("TAG", "onFailure: ") })



    }

    override fun getContentLayout(): Int {
        return R.layout.activity_manage_user
    }

    override fun initListener() {
       binding.imvBack.setOnClickListener { finish() }
        binding.imgPlus.setOnClickListener { var intent=Intent(this,AddUserActivity::class.java)
        intent.putExtra(TOTAL_USER, size)
        startActivity(intent)}
    }


    override fun deleteUser(position: Int) {
        userPresenter.deleteUser(position)
        adapter.deleteUser(position)
    }


    override fun notify(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show()
    }

    override fun complete() {
        finish()
    }


}