package com.example.footapp.ui.customer

import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.footapp.R
import com.example.footapp.ViewModelFactory
import com.example.footapp.databinding.ActivityCustomerBinding
import com.example.footapp.model.DetailItemChoose
import com.example.footapp.presenter.CustomerViewModel
import com.example.footapp.ui.BaseActivity
import com.example.footapp.ui.Order.ItemChooseAdapter

class CustomerActivity : BaseActivity<ActivityCustomerBinding, CustomerViewModel>() {
    lateinit var itemChooseAdapter: ItemChooseAdapter
    private val itemList: MutableList<DetailItemChoose> = mutableListOf()
    override fun observerData() {
        viewModel.repository.data.observe(this) {
            receiveData(it)
        }
    }

    override fun getContentLayout(): Int {
        return R.layout.activity_customer
    }

    override fun initView() {
        itemChooseAdapter = ItemChooseAdapter(this, itemList)
        binding.rcItem.layoutManager = LinearLayoutManager(this)
        binding.rcItem.adapter = itemChooseAdapter
    }

    override fun initListener() {
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(this, ViewModelFactory(this))[CustomerViewModel::class.java]
    }

    private fun receiveData(item: DetailItemChoose) {
        if (item.flag == true) {
            if (itemList.find { it.id == item.id } == null) {
                itemList.add(item)
                itemChooseAdapter.notifyItemInserted(itemList.size)
            } else {
                for (i in 0 until itemList.size) {
                    if (itemList[i].id == item.id) {
                        itemList[i] = item
                        itemChooseAdapter.notifyItemChanged(i)
                        break
                    }
                }
            }
        } else {
            val index = itemList.indexOf(itemList.find { it.id == item.id })
            itemList.removeAt(index)
            itemChooseAdapter.notifyItemRemoved(index)
        }
    }
}
