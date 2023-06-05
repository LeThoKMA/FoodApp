package com.example.footapp.ui.Order

import android.os.Bundle
import android.os.Parcelable
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.footapp.base.BaseFragment
import com.example.footapp.R
import com.example.footapp.ViewModelFactory
import com.example.footapp.databinding.HomeFragmentBinding
import com.example.footapp.interface1.OrderInterface
import com.example.footapp.model.DetailItemChoose
import com.example.footapp.model.Item
import com.example.footapp.utils.BILL_RESPONSE
import com.example.footapp.utils.ITEMS_PICKED
import com.google.gson.Gson

class HomeFragment(val onChangeFragment: (Bundle) -> Unit) :
    BaseFragment<HomeFragmentBinding, OrderViewModel>(), OrderInterface {
    var listItem: ArrayList<Item?> = arrayListOf()
    var listItemChoose: MutableList<DetailItemChoose> = mutableListOf()
    lateinit var oderAdapter: OderAdapter
    lateinit var itemChooseAdapter: ItemChooseAdapter

    override fun getContentLayout(): Int {
        return R.layout.home_fragment
    }

    override fun initView() {
        paddingStatusBar(binding.root)
        oderAdapter = OderAdapter(listItem, this)
        binding.rvCategory.layoutManager = LinearLayoutManager(binding.root.context)
        binding.rvCategory.adapter = oderAdapter

        itemChooseAdapter = ItemChooseAdapter(binding.root.context, listItemChoose)
        binding.rvItemPick.layoutManager = LinearLayoutManager(binding.root.context)
        binding.rvItemPick.adapter = itemChooseAdapter
    }

    override fun initListener() {
        binding.tvCreate.setOnClickListener {
            viewModel.payConfirm(listItemChoose)
        }
    }

    override fun addItemToBill(item: DetailItemChoose) {
        viewModel.addItemToBill(item)
        if (item.flag == true) {
            if (listItemChoose.find { it.id == item.id } == null) {
                listItemChoose.add(item)
                itemChooseAdapter.notifyItemInserted(listItemChoose.size)
            } else {
                for (i in 0 until listItemChoose.size) {
                    if (listItemChoose[i].id == item.id) {
                        listItemChoose[i] = item
                        itemChooseAdapter.notifyItemChanged(i)
                        break
                    }
                }
            }
        } else {
            if (listItemChoose.find { it.id == item.id } == null) return
            val index = listItemChoose.indexOf(listItemChoose.find { it.id == item.id })
            listItemChoose.removeAt(index)
            itemChooseAdapter.notifyItemRemoved(index)
        }
        viewModel.repository.sendData(item)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(binding.root.context),
        )[OrderViewModel::class.java]
    }

    override fun observerLiveData() {
        viewModel.dataItems.observe(viewLifecycleOwner) {
            if (it != null) {
                listItem.clear()
                listItem.addAll(it)
                oderAdapter.resetData()
            }
        }
        viewModel.price.observe(viewLifecycleOwner) {
            binding.tvPrice.text = it.toString() + "đ"
        }
        viewModel.message.observe(viewLifecycleOwner) {
            Toast.makeText(binding.root.context, it, Toast.LENGTH_LONG).show()
        }
        viewModel.confirm.observe(viewLifecycleOwner) {
            if (it != null) {
                it.let { it1 -> viewModel.repository.getBillResponse(it1) }
                val bundle = Bundle()
                bundle.putString(BILL_RESPONSE, Gson().toJson(it))
                bundle.putParcelableArrayList(
                    ITEMS_PICKED,
                    listItemChoose as java.util.ArrayList<out Parcelable>,
                )
               // viewModel.isLoading.value = false
                onChangeFragment.invoke(bundle)
            }
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            viewModel.fetchItems()
            listItemChoose.clear()
            itemChooseAdapter.notifyDataSetChanged()
        }
    }
}
