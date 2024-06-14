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

class HomeFragment() : BaseFragment<HomeFragmentBinding, OrderViewModel>(), OrderInterface,
    ItemPickedInterface {
    private val mainViewModel: MainViewModel by activityViewModels()
    private var listItem: ArrayList<Item?> = arrayListOf()
    private var listItemChoose: MutableList<DetailItemChoose> = mutableListOf()
    private var listCategory = mutableListOf<CategoryResponse>()
    private var oderAdapter: OderAdapter? = null
    private var itemChooseAdapter: ItemChooseAdapter? = null
    private var categoryAdapter: CategoryAdapter? = null

    override fun getContentLayout(): Int {
        return R.layout.home_fragment
    }

    override fun initView() {
        val binding = binding!!
        binding.let { paddingStatusBar(it.root) }
        oderAdapter = OderAdapter(listItem, this)
        binding.rvCategory.adapter = oderAdapter

        itemChooseAdapter = ItemChooseAdapter(listItemChoose, this)
        binding.rvItemPick.layoutManager = LinearLayoutManager(requireContext())
        binding.rvItemPick.adapter = itemChooseAdapter

        categoryAdapter = CategoryAdapter(listCategory, onClickItem = { id ->
            listCategory.forEach { if (it.id != id) it.isPicked = false }
            listCategory[id].isPicked = true
            categoryAdapter?.notifyDataSetChanged()
            viewModel.getProductByType(id)
        })
        binding.rvType.adapter = categoryAdapter
    }

    override fun initListener() {
        binding?.tvCreate?.setOnClickListener {
            viewModel.payConfirm(listItemChoose)
        }
    }

    override fun addItemToBill(item: Item) {
        val detailItemChoose =
            DetailItemChoose(item.id, item.name, 1, item.price, item.imgUrl)
        listItemChoose.add(detailItemChoose)
        itemChooseAdapter?.notifyItemInserted(listItemChoose.size - 1)
        viewModel.addItemToBill(detailItemChoose)
        viewModel.repository.addItem(detailItemChoose)
        binding?.rvItemPick?.scrollToPosition(listItemChoose.size - 1)
    }

    override fun removeItem(item: Item) {
        val index = listItemChoose.indexOfFirst { it.id == item.id }
        val detailItemChoose = listItemChoose.find { it.id == item.id }
        listItemChoose.removeAt(index)
        itemChooseAdapter?.notifyItemRemoved(index)
        viewModel.removeItemInBill(item.id)
        detailItemChoose?.let { viewModel.repository.removeItem(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(),
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
            it?.let { it1 ->
                listCategory.add(CategoryResponse(0, "Tất cả", true))
                listCategory.addAll(it1)
            }
            categoryAdapter?.notifyDataSetChanged()
        }
        mainViewModel.paySuccess.observe(viewLifecycleOwner){
            listItemChoose.clear()
            itemChooseAdapter?.notifyDataSetChanged()
            oderAdapter?.resetData()
            binding?.tvPrice?.text = 0.formatToPrice()
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
        }
    }

    override fun onPause() {
        super.onPause()
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

    override fun plus(detailItemChoose: DetailItemChoose) {
        val newItem = listItemChoose.find { it.id == detailItemChoose.id }?.also { it.count++ }
        itemChooseAdapter?.notifyItemChanged(listItemChoose.indexOf(newItem))
        binding?.rvItemPick?.scrollToPosition(listItemChoose.indexOf(newItem))

        newItem?.let { viewModel.addItemToBill(it) }
        if (newItem != null) {
            viewModel.repository.addItem(newItem)
        }
    }

    override fun minus(detailItemChoose: DetailItemChoose) {
        if (detailItemChoose.count > 1) {
            val newItem = listItemChoose.find { it.id == detailItemChoose.id }?.also { it.count-- }
            itemChooseAdapter?.notifyItemChanged(listItemChoose.indexOf(newItem))
            binding?.rvItemPick?.scrollToPosition(listItemChoose.indexOf(newItem))
            newItem?.let { viewModel.addItemToBill(it) }
            newItem?.let { viewModel.repository.addItem(it) }
        }
    }
}
