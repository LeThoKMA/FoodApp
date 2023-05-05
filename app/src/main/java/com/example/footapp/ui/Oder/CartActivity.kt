package com.example.footapp.ui.Oder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.footapp.R
import com.example.footapp.ViewModelFactory
import com.example.footapp.databinding.ActivityCartBinding
import com.example.footapp.interface1.OrderInterface
import com.example.footapp.model.DetailItemChoose
import com.example.footapp.model.Item
import com.example.footapp.presenter.OrderViewModel
import com.example.footapp.ui.BaseActivity
import com.example.footapp.ui.pay.PayConfirmActivity
import com.example.footapp.utils.MAP
import com.example.footapp.utils.TABLE_ACTION
import com.example.footapp.utils.TABLE_POSITION
import com.example.footapp.utils.TOTAL_PRICE

class CartActivity : BaseActivity<ActivityCartBinding, OrderViewModel>(), OrderInterface {
    var tablePos = 0
    var listItem: ArrayList<Item?> = arrayListOf()
    lateinit var oderAdapter: OderAdapter
    var broadcastReceiver = object : BroadcastReceiver() {
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
        var intentFilter = IntentFilter(TABLE_ACTION)
        registerReceiver(broadcastReceiver, intentFilter)
        tablePos = intent.getIntExtra(TABLE_POSITION, 0)
        viewModel.getDataItem()
        loadingDialog?.show()
        oderAdapter = OderAdapter(listItem, this)
        binding.rvCategory.layoutManager = LinearLayoutManager(this)
        binding.rvCategory.adapter = oderAdapter
    }

    override fun initListener() {
        binding.tvCreate.setOnClickListener {
            viewModel.payConfirm()
        }
        binding.imvBack.setOnClickListener {
            finish()
        }
    }

    override fun addItemToBill(item: DetailItemChoose) {
        viewModel.addItemToBill(item)
    }

    override fun onDestroy() {
        unregisterReceiver(broadcastReceiver)
        super.onDestroy()
    }

    override fun observerData() {
        viewModel.dataItems.observe(this@CartActivity) {
            if (it != null) {
                listItem.clear()
                listItem.addAll(it)
                oderAdapter.notifyDataSetChanged()
                loadingDialog?.dismiss()
            }
        }
        viewModel.dataChange.observe(this) {
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
        viewModel.price.observe(this) {
            binding.tvPrice.text = it.toString() + "đ"
        }
        viewModel.message.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }
        viewModel.confirm.observe(this) {
            val intent = Intent(this, PayConfirmActivity::class.java)
            intent.putExtra(MAP, it.first)
            intent.putExtra(TOTAL_PRICE, it.second)
            intent.putExtra(TABLE_POSITION, tablePos)
            startActivity(intent)
        }
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(this, ViewModelFactory(this))[OrderViewModel::class.java]
    }
}
