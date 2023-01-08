package com.example.footapp.ui.Oder

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.footapp.R
import com.example.footapp.`interface`.OderInterface
import com.example.footapp.databinding.ActivityCartBinding
import com.example.footapp.model.DetailItemChoose
import com.example.footapp.model.Item
import com.example.footapp.presenter.OderPresenter
import com.example.footapp.ui.BaseActivity
import com.example.footapp.ui.pay.PayConfirmActivity
import com.google.firebase.database.DatabaseReference

class CartActivity : BaseActivity<ActivityCartBinding>(), OderInterface {
    var tablePos=0
    var listItem: ArrayList<Item?> = arrayListOf()
    lateinit var oderPresenter: OderPresenter
    lateinit var oderAdapter: OderAdapter
    override fun getContentLayout(): Int {
        return R.layout.activity_cart
    }

    override fun initView() {

        tablePos=intent.getIntExtra("pos_table",0)
        oderPresenter = OderPresenter(this, this, this@CartActivity)
        oderPresenter.getDataItem()
        loadingDialog?.show()
        oderAdapter = OderAdapter(listItem, oderPresenter)
        binding.rvCategory.layoutManager = LinearLayoutManager(this)
        binding.rvCategory.adapter = oderAdapter

        oderPresenter.dataItems.observe(this@CartActivity)
        {
            if (it != null) {
                listItem.clear()
                listItem.addAll(it)
                oderAdapter.notifyDataSetChanged()
                loadingDialog?.dismiss()
            }
        }
        oderPresenter.dataChange.observe(this)
        {
            if(it!=null)
            {
                for(item in listItem)
                {
                    if(it.id==item?.id)
                    {
                        item?.amount=it.amount
                        item?.price=it.price
                        break
                    }
                }
                oderAdapter.notifyDataSetChanged()

            }
        }


    }


    override fun initListener() {
        binding.tvCreate.setOnClickListener {
            oderPresenter.payConfirm()
        }
        binding.imvBack.setOnClickListener{
            finish()
        }
    }

    override fun price(priceItem: Int) {
        binding.tvPrice.text = priceItem.toString() + "đ"
    }


    override fun complete(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    @SuppressLint("SuspiciousIndentation")
    override fun confirm(map: HashMap<Int, DetailItemChoose>, totalPrice: Int) {
        var intent = Intent(this, PayConfirmActivity::class.java)
        intent.putExtra("map", map)
        intent.putExtra("totalPrice", totalPrice)
        intent.putExtra("pos_table",tablePos)
        startActivity(intent)
    }

}