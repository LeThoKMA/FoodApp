package com.example.footapp.ui.Oder

import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.footapp.DAO.DAO
import com.example.footapp.R
import com.example.footapp.databinding.ActivityCartBinding
import com.example.footapp.model.Bill
import com.example.footapp.model.Item
import com.example.footapp.model.User
import com.example.footapp.ui.BaseActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import java.util.logging.Handler

class CartActivity :BaseActivity<ActivityCartBinding>(),OderInterface
{
    lateinit var databaseReference: DatabaseReference
    var listItem:ArrayList<Item?> = arrayListOf()
    lateinit var oderPresenter: OderPresenter
    lateinit var oderAdapter: OderAdapter
    override fun getContentLayout(): Int {
        return R.layout.activity_cart
    }

    override fun initView() {

       databaseReference= dao.itemReference
        oderPresenter=OderPresenter(this,this,this@CartActivity)
        oderPresenter.getItems()
        loadingDialog?.show()
        oderAdapter= OderAdapter(listItem,oderPresenter)
        binding.rvCategory.layoutManager=LinearLayoutManager(this)
        binding.rvCategory.adapter=oderAdapter

        oderPresenter.dataItems.observe(this@CartActivity)
        {
         if(it!=null)
         {
             listItem.addAll(it)
             Log.e("TAG", listItem.toString())
             oderAdapter.notifyDataSetChanged()
             loadingDialog?.dismiss()
         }
        }

//        val itemListener = object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                // Get Post object and use the values to update the UI
//                loadingDialog?.show()
//                listItem.clear()
//
//                dataSnapshot.getValue<List<Item>>()?.let { listItem.addAll(it) }
////                list.addAll(
////                    Gson().fromJson(
////                        dataSnapshot.value.toString(),
////                        object : TypeToken<List<User?>?>() {}.type
////                    )
////
////                )
//            //    size=list.size
//                oderAdapter.notifyDataSetChanged()
//                loadingDialog?.dismiss()
//
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                // Getting Post failed, log a message
//                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
//            }
//        }
//        databaseReference.addValueEventListener(itemListener)

    }



    override fun initListener() {
        binding.tvCreate.setOnClickListener{
            oderPresenter.payConfirm()
        }
    }

    override fun price(priceItem:Int) {
        binding.tvPrice.text=priceItem.toString()+"đ"
    }

    override fun deleteCart(position: Int) {
    }

    override fun hideKeyboard() {
    }

    override fun showLog(content: String) {
    }

    override fun editTextListener(position: Int, data: Item) {
    }

    override fun minusPrice() {
    }

    override fun complete(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show()
    }

    override fun confirm(bill: Bill) {

    }

}