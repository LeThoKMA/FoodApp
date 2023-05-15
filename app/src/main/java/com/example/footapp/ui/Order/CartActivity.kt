package com.example.footapp.ui.Order

import android.app.ActivityOptions
import android.content.Intent
import android.hardware.display.DisplayManager
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
import com.example.footapp.ui.customer.CustomerActivity
import com.example.footapp.ui.pay.PayConfirmActivity
import com.example.footapp.utils.BILL_RESPONSE
import com.example.footapp.utils.ITEMS_PICKED
import com.google.gson.Gson

class CartActivity : BaseActivity<ActivityCartBinding, OrderViewModel>(), OrderInterface {
    var listItem: ArrayList<Item?> = arrayListOf()
    var listItemChoose: MutableList<DetailItemChoose> = mutableListOf()
    lateinit var oderAdapter: OderAdapter
    lateinit var itemChooseAdapter: ItemChooseAdapter

    override fun getContentLayout(): Int {
        return R.layout.activity_cart
    }

    override fun initView() {
        setColorForStatusBar(R.color.colorPrimary)
        setLightIconStatusBar(false)
        oderAdapter = OderAdapter(listItem, this)
        binding.rvCategory.layoutManager = LinearLayoutManager(this)
        binding.rvCategory.adapter = oderAdapter

        itemChooseAdapter = ItemChooseAdapter(this, listItemChoose)
        binding.rvItemPick.layoutManager = LinearLayoutManager(this)
        binding.rvItemPick.adapter = itemChooseAdapter
    }

    override fun initListener() {
        binding.tvCreate.setOnClickListener {
            viewModel.payConfirm(listItemChoose)
        }
        binding.imvBack.setOnClickListener {
            finish()
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

    override fun observerData() {
        viewModel.dataItems.observe(this@CartActivity) {
            if (it != null) {
                listItem.clear()
                listItem.addAll(it)
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
            intent.putExtra(BILL_RESPONSE, it)
            intent.putExtra(ITEMS_PICKED, Gson().toJson(listItemChoose))
            startActivity(intent)
        }
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(this, ViewModelFactory(this))[OrderViewModel::class.java]
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
}
