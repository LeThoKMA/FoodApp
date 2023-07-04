package com.example.footapp.ui.customer

import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.footapp.R
import com.example.footapp.ViewModelFactory
import com.example.footapp.base.BaseActivity
import com.example.footapp.databinding.ActivityCustomerBinding
import com.example.footapp.model.DetailItemChoose
import com.example.footapp.ui.Order.ItemChooseAdapter
import com.example.footapp.utils.formatToPrice
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class CustomerActivity : BaseActivity<ActivityCustomerBinding, CustomerViewModel>() {
    lateinit var itemChooseAdapter: ItemChooseAdapter
    private val itemList: MutableList<DetailItemChoose> = mutableListOf()
    lateinit var bannerFragmentStateAdapter: BannerFragmentStateAdapter

    override fun observerData() {
        viewModel.repository.data.observe(this) {
            receiveData(it)
        }
        viewModel.repository.billResponse.observe(this) {
            val priceDiscount = (it.promotion.div(100f)).times(it.totalPrice!!).toInt()
            binding.tvPromotionDiscount.text = priceDiscount.formatToPrice()
            binding.tvPrice.text = it.totalPrice.minus(priceDiscount).formatToPrice()
        }
        viewModel.repository.resetData.observe(this) {
            if (it) {
                itemList.clear()
                itemChooseAdapter.notifyDataSetChanged()
                binding.tvPromotionDiscount.text = ""
                binding.tvPrice.text = ""
            }
        }
        viewModel.data.observe(this) {
            bannerFragmentStateAdapter = BannerFragmentStateAdapter(it as ArrayList, this)
            binding.viewPager.adapter = bannerFragmentStateAdapter
            var position = 0
            lifecycleScope.launch {
                while (isActive) {
                    binding.viewPager.setCurrentItem(position % (it.size), true)
                    position++
                    delay(4000)
                }
            }
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

    override fun onResume() {
        super.onResume()
    }
}
