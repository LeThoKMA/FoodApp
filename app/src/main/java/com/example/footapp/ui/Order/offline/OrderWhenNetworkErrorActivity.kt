package com.example.footapp.ui.Order.offline

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.hardware.display.DisplayManager
import android.os.Parcelable
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.footapp.R
import com.example.footapp.Response.BillResponse
import com.example.footapp.Response.CategoryResponse
import com.example.footapp.Response.QrData
import com.example.footapp.Response.QrResponse
import com.example.footapp.ViewModelFactory
import com.example.footapp.base.BaseActivity
import com.example.footapp.databinding.HomeOfflineActivityBinding
import com.example.footapp.model.DetailItemChoose
import com.example.footapp.model.Item
import com.example.footapp.ui.Order.CategoryAdapter
import com.example.footapp.ui.Order.ItemChooseAdapter
import com.example.footapp.ui.Order.ItemPickedInterface
import com.example.footapp.ui.Order.OderAdapter
import com.example.footapp.ui.Order.OrderInterface
import com.example.footapp.ui.customer.CustomerActivity
import com.example.footapp.utils.ITEMS_PICKED
import com.example.footapp.utils.TOTAL_PRICE
import com.example.footapp.utils.formatToPrice
import com.example.footapp.utils.toast

class OrderWhenNetworkErrorActivity :
    BaseActivity<HomeOfflineActivityBinding, OrderWhenNetworkErrorViewModel>(), OrderInterface,
    ItemPickedInterface {
    private var listItem: ArrayList<Item?> = arrayListOf()
    private var listItemChoose: MutableList<DetailItemChoose> = mutableListOf()
    private var listCategory = mutableListOf<CategoryResponse>()
    private var oderAdapter: OderAdapter? = null
    private var itemChooseAdapter: ItemChooseAdapter? = null
    private var categoryAdapter: CategoryAdapter? = null
    private var startForResult: ActivityResultLauncher<Intent>? = null

    override fun observerData() {
        viewModel.dataItems.observe(this) {
            if (it != null) {
                listItem.clear()
                listItem.addAll(it)
                oderAdapter?.resetData()
            }
        }
        viewModel.message.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }
        viewModel.category.observe(this) {
            listCategory.clear()
            it?.let { it1 ->
                listCategory.add(CategoryResponse(0, "Tất cả", true))
                listCategory.addAll(it1)
            }
            categoryAdapter?.notifyDataSetChanged()
        }
        viewModel.price.observe(this) {
            binding.tvPrice.text = it.formatToPrice()
        }
    }

    override fun getContentLayout(): Int {
        return R.layout.home_offline_activity
    }

    override fun initView() {
        startForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == Activity.RESULT_OK) {
                    listItemChoose.clear()
                    itemChooseAdapter?.notifyDataSetChanged()
                    oderAdapter?.resetData()
                    viewModel.repository.resetData()
                    binding.tvPrice.text = 0.formatToPrice()
                }
            }
        oderAdapter = OderAdapter(listItem, this)
        binding.rvCategory.adapter = oderAdapter

        itemChooseAdapter = ItemChooseAdapter(listItemChoose, this)
        binding.rvItemPick.layoutManager = LinearLayoutManager(this)
        binding.rvItemPick.adapter = itemChooseAdapter

        categoryAdapter = CategoryAdapter(listCategory, onClickItem = {id ->
            listCategory.forEach { if (it.id != id) it.isPicked = false }
            listCategory[id].isPicked = true
            categoryAdapter?.notifyDataSetChanged()
            viewModel.getProductByType(id)
        })
        binding.rvType.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvType.adapter = categoryAdapter
        showScreenCustomer()
    }

    override fun initListener() {
        binding.tvCreate.setOnClickListener {
            if (viewModel.totalPrice > 0) {
                val tmpBillResponse = BillResponse(
                    totalPrice = viewModel.totalPrice,
                    qrResponse = QrResponse(data = QrData(qrDataURL = viewModel.qrDefault))
                )
                val intent = Intent(this, OfflineConfirmActivity::class.java)
                intent.putExtra(
                    ITEMS_PICKED,
                    listItemChoose as java.util.ArrayList<out Parcelable>,
                )
                intent.putExtra(TOTAL_PRICE, viewModel.totalPrice)
                startForResult?.launch(intent)
                viewModel.repository.getBillResponse(tmpBillResponse)
            } else {
                toast("Hãy chọn đồ uống")
            }
        }
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(),
        )[OrderWhenNetworkErrorViewModel::class.java]
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

    private fun showScreenCustomer() {
        val displayManager = getSystemService(DISPLAY_SERVICE) as DisplayManager
        val displays = displayManager.displays
        if (displays.size > 1) {
            // Activity options are used to select the display screen.
            val options = ActivityOptions.makeBasic()

            // Select the display screen that you want to show the second activity
            options.launchDisplayId = displays[1].displayId
            // To display on the second screen that your intent must be set flag to make
            // single task (combine FLAG_ACTIVITY_CLEAR_TOP and FLAG_ACTIVITY_NEW_TASK)
            // or you also set it in the manifest (see more at the manifest file)
            startActivity(
                Intent(this, CustomerActivity::class.java),
                options.toBundle(),
            )
        }
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

