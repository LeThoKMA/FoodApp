package com.example.footapp.ui.Order.offline

import android.content.Intent
import android.os.Parcelable
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.footapp.MainActivity
import com.example.footapp.R
import com.example.footapp.Response.CategoryResponse
import com.example.footapp.ViewModelFactory
import com.example.footapp.base.BaseActivity
import com.example.footapp.databinding.HomeOfflineActivityBinding
import com.example.footapp.model.DetailItemChoose
import com.example.footapp.model.Item
import com.example.footapp.ui.Order.CategoryAdapter
import com.example.footapp.ui.Order.ItemChooseAdapter
import com.example.footapp.ui.Order.OderAdapter
import com.example.footapp.ui.Order.OrderInterface
import com.example.footapp.utils.ITEMS_PICKED
import com.example.footapp.utils.TOTAL_PRICE
import com.example.footapp.utils.toast

class OrderWhenNetworkErrorActivity :
    BaseActivity<HomeOfflineActivityBinding, OrderWhenNetworkErrorViewModel>(), OrderInterface {
    private var listItem: ArrayList<Item?> = arrayListOf()
    private var listItemChoose: MutableList<DetailItemChoose> = mutableListOf()
    private var listCategory = mutableListOf<CategoryResponse>()
    private var oderAdapter: OderAdapter? = null
    private var itemChooseAdapter: ItemChooseAdapter? = null
    private var categoryAdapter: CategoryAdapter? = null
    override fun observerData() {
        TODO("Not yet implemented")
    }

    override fun getContentLayout(): Int {
        return R.layout.home_offline_activity
    }

    override fun initView() {
        oderAdapter = OderAdapter(listItem, this)
        binding.rvCategory.layoutManager = LinearLayoutManager(this)
        binding.rvCategory.adapter = oderAdapter

        itemChooseAdapter = ItemChooseAdapter()
        binding.rvItemPick.layoutManager = LinearLayoutManager(this)
        binding.rvItemPick.adapter = itemChooseAdapter

        categoryAdapter = CategoryAdapter(listCategory, onClickItem = {
            viewModel.getProductByType(it)
        })
        binding.rvType.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvType.adapter = categoryAdapter
    }

    override fun initListener() {
        binding.tvCreate.setOnClickListener {
            if (viewModel.totalPrice > 0) {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra(
                    ITEMS_PICKED,
                    listItemChoose as java.util.ArrayList<out Parcelable>,
                )
                intent.putExtra(TOTAL_PRICE, viewModel.totalPrice)
                startActivity(intent)
            } else {
                toast("Hãy chọn món ăn")
            }
        }
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(),
        )[OrderWhenNetworkErrorViewModel::class.java]
    }

    override fun addItemToBill(item: DetailItemChoose) {
        viewModel.addItemToBill(item)
        if (item.flag == true) {
            if (listItemChoose.find { it.id == item.id } == null) {
                listItemChoose.add(item)
            } else {
                for (i in 0 until listItemChoose.size) {
                    if (listItemChoose[i].id == item.id) {
                        listItemChoose[i] = item
                        break
                    }
                }
            }
        } else {
            if (listItemChoose.find { it.id == item.id } == null) return
            val index = listItemChoose.indexOf(listItemChoose.find { it.id == item.id })
            listItemChoose.removeAt(index)
        }
        val list = listItemChoose.toList()
        itemChooseAdapter?.submitList(list)
        viewModel.repository.sendData(item)
    }
}
