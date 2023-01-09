package com.example.footapp.ui.Oder

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.footapp.R
import com.example.footapp.databinding.ActivityCartBinding
import com.example.footapp.interface1.OderInterface
import com.example.footapp.model.DetailItemChoose
import com.example.footapp.model.Item
import com.example.footapp.presenter.OderPresenter
import com.example.footapp.ui.BaseActivity
import com.example.footapp.ui.pay.PayConfirmActivity
import com.example.footapp.utils.MAP
import com.example.footapp.utils.TABLE_ACTION
import com.example.footapp.utils.TABLE_POSITION
import com.example.footapp.utils.TOTAL_PRICE

class CartActivity : BaseActivity<ActivityCartBinding>(), OderInterface {
    var tablePos = 0
    var listItem: ArrayList<Item?> = arrayListOf()
    lateinit var oderPresenter: OderPresenter
    lateinit var oderAdapter: OderAdapter
    var broadcastReceiver= object :BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {
            finish()
        }

    }
    override fun getContentLayout(): Int {
        return R.layout.activity_cart
    }

    override fun initView() {
        setColorForStatusBar(R.color.colorPrimary)
        setLightIconStatusBar(false)
        var intentFilter=IntentFilter(TABLE_ACTION)
        registerReceiver(broadcastReceiver,intentFilter)
        tablePos = intent.getIntExtra(TABLE_POSITION, 0)
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
            if (it != null) {
                for (item in listItem) {
                    if (it.id == item?.id) {
                        item?.amount = it.amount
                        item?.price = it.price
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
        binding.imvBack.setOnClickListener {
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
        intent.putExtra(MAP, map)
        intent.putExtra(TOTAL_PRICE, totalPrice)
        intent.putExtra(TABLE_POSITION, tablePos)
        startActivity(intent)
    }

    override fun onDestroy() {
        unregisterReceiver(broadcastReceiver)
        super.onDestroy()
    }

}