package com.example.footapp.ui.Order

import android.os.Bundle
import android.os.Parcelable
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.footapp.MainViewModel
import com.example.footapp.R
import com.example.footapp.Response.BillResponse
import com.example.footapp.Response.CategoryResponse
import com.example.footapp.ViewModelFactory
import com.example.footapp.base.BaseFragment
import com.example.footapp.databinding.HomeFragmentBinding
import com.example.footapp.model.DetailItemChoose
import com.example.footapp.model.Item
import com.example.footapp.utils.BILL_RESPONSE
import com.example.footapp.utils.ITEMS_PICKED
import com.example.footapp.utils.formatToPrice
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragment() : BaseFragment<HomeFragmentBinding, OrderViewModel>(), OrderInterface {
    private val mainViewModel: MainViewModel by activityViewModels()
    var listItem: ArrayList<Item?> = arrayListOf()
    var listItemChoose: MutableList<DetailItemChoose> = mutableListOf()
    var listCategory = mutableListOf<CategoryResponse>()
    var oderAdapter: OderAdapter? = null
    var itemChooseAdapter: ItemChooseAdapter? = null
    var categoryAdapter: CategoryAdapter? = null

    override fun getContentLayout(): Int {
        return R.layout.home_fragment
    }

    override fun initView() {
        val binding = binding!!
        binding.let { paddingStatusBar(it.root) }
        oderAdapter = OderAdapter(listItem, this)
        binding.rvCategory.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCategory.adapter = oderAdapter

        itemChooseAdapter = ItemChooseAdapter()
        binding.rvItemPick.layoutManager = LinearLayoutManager(requireContext())
        binding.rvItemPick.adapter = itemChooseAdapter

        categoryAdapter = CategoryAdapter(listCategory, onClickItem = {
            viewModel.getProductByType(it)
        })
        binding.rvType.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvType.adapter = categoryAdapter
    }

    override fun initListener() {
        binding?.tvCreate?.setOnClickListener {
            viewModel.payConfirm(listItemChoose)
        }
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

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(requireContext()),
        )[OrderViewModel::class.java]
    }

    override fun observerLiveData() {
        viewModel.dataItems.observe(viewLifecycleOwner) {
            if (it != null) {
                listItem.clear()
                listItem.addAll(it)
                oderAdapter?.resetData()
            }
        }
        viewModel.message.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.price.collect {
                binding?.tvPrice?.text = it.formatToPrice()
            }
        }

        lifecycleScope.launch(Dispatchers.Main) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.confirm.collect(::handleEvent)
            }
        }

        viewModel.category.observe(viewLifecycleOwner) {
            listCategory.clear()
            it?.let { it1 -> listCategory.addAll(it1) }
            categoryAdapter?.notifyDataSetChanged()
        }
    }

    private fun handleEvent(event: OrderViewModel.Event) {
        when (event) {
            is OrderViewModel.Event.OnConfirmSuccess -> {
                if (event.response != BillResponse()) {
                    event.response.let { it1 -> viewModel.repository.getBillResponse(it1) }
                    val bundle = Bundle()
                    val tmpBill = BillResponse(
                        event.response.id,
                        event.response.totalPrice,
                        event.response.givenPromotion,
                    )
                    bundle.putString(BILL_RESPONSE, Gson().toJson(tmpBill))
                    bundle.putParcelableArrayList(
                        ITEMS_PICKED,
                        listItemChoose as java.util.ArrayList<out Parcelable>,
                    )
                    mainViewModel.gotoPayFragment(bundle)
                    findNavController().navigate(R.id.pay_dest)
                }
            }
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            listItemChoose.clear()
            itemChooseAdapter?.submitList(listItemChoose)
            oderAdapter?.resetData()
            viewModel.repository.resetData()
            binding?.tvPrice?.text = 0.formatToPrice()
        }
    }

    override fun onPause() {
        super.onPause()

//        if (isRemoving) {
//            binding.unbind()
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        categoryAdapter = null
        itemChooseAdapter = null
        oderAdapter = null
    }

    override fun onStop() {
        super.onStop()
    }
}
