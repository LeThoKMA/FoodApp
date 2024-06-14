package com.example.footapp.ui.customer

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.footapp.R
import com.example.footapp.ViewModelFactory
import com.example.footapp.base.BaseActivityForCustomer
import com.example.footapp.databinding.ActivityCustomerBinding
import com.example.footapp.model.DetailItemChoose
import com.example.footapp.ui.Order.ItemChooseAdapter
import com.example.footapp.ui.Order.ItemPickedInterface
import com.example.footapp.utils.formatToPrice
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CustomerActivity : BaseActivityForCustomer<ActivityCustomerBinding, CustomerViewModel>() {
    private lateinit var itemChooseAdapter: ItemChooseCustomerAdapter
    private val itemList: MutableList<DetailItemChoose> = mutableListOf()
    private lateinit var bannerFragmentStateAdapter: BannerFragmentStateAdapter

    override fun observerData() {
        viewModel.repository.dataAdd.observe(this) {
            receiveData(it)
        }
        viewModel.repository.dataRemove.observe(this) { data ->
            val index = itemList.indexOf(itemList.find { it.id == data.id })
            itemList.removeAt(index)
            itemChooseAdapter.notifyItemRemoved(index)
        }
        viewModel.repository.billResponse.observe(this) {
            val priceDiscount = (it.promotion.div(100f)).times(it.totalPrice!!).toInt()
            binding.tvPromotionDiscount.text = priceDiscount.formatToPrice()
            binding.tvPrice.text = it.totalPrice.minus(priceDiscount).formatToPrice()
            lifecycleScope.launch(IO) {
                val qrData = Base64.decode(it.qrResponse?.data?.qrDataString, Base64.DEFAULT)
                withContext(Main) {
                    val qrDialog = QrDialog()
                    val bundle = Bundle()
                    bundle.putByteArray("qr", qrData)
                    qrDialog.arguments = bundle
                    qrDialog.show(supportFragmentManager, "QR_DIALOG")
                }
            }
        }
        viewModel.repository.resetData.observe(this) {
            if (it) {
                itemList.clear()
                itemChooseAdapter.notifyDataSetChanged()
                binding.tvPromotionDiscount.text = ""
                binding.tvPrice.text = ""
                (supportFragmentManager.findFragmentByTag("QR_DIALOG") as? DialogFragment)?.dismiss()
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
        itemChooseAdapter = ItemChooseCustomerAdapter(itemList)
        binding.rcItem.layoutManager = LinearLayoutManager(this)
        binding.rcItem.adapter = itemChooseAdapter
        if (checkInitialNetworkStatus()) {
            viewModel.getBannerList()
        } else {
            viewModel.getBannerListOffline()
        }
    }

    override fun initListener() {
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(this, ViewModelFactory())[CustomerViewModel::class.java]
    }

    private fun receiveData(item: DetailItemChoose) {
        if (itemList.find { it.id == item.id } == null) {
            itemList.add(item)
        } else {
            for (i in 0 until itemList.size) {
                if (itemList[i].id == item.id) {
                    itemList[i] = item
                    break
                }
            }
        }
        itemChooseAdapter.notifyItemChanged(itemList.indexOf(item))
        binding.rcItem.scrollToPosition(itemList.indexOf(item))

    }

    override fun onResume() {
        super.onResume()
    }

    private fun checkInitialNetworkStatus(): Boolean {
        val network = applicationContext.getSystemService(
            ConnectivityManager::class.java
        ).activeNetwork
        val networkCapabilities = applicationContext.getSystemService(
            ConnectivityManager::class.java
        ).getNetworkCapabilities(network)
        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }

    override fun onDestroy() {
        itemList.clear()
        itemChooseAdapter.notifyDataSetChanged()
        binding.tvPromotionDiscount.text = ""
        binding.tvPrice.text = ""
        (supportFragmentManager.findFragmentByTag("QR_DIALOG") as? DialogFragment)?.dismiss()
        super.onDestroy()
    }
}
