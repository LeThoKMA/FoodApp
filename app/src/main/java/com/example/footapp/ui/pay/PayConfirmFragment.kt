package com.example.footapp.ui.pay

import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.footapp.*
import com.example.footapp.Response.BillResponse
import com.example.footapp.base.BaseFragment
import com.example.footapp.databinding.ActivityPayConfirmBinding
import com.example.footapp.model.DetailItemChoose
import com.example.footapp.utils.*
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*

class PayConfirmFragment(val onSuccess: () -> Unit) :
    BaseFragment<ActivityPayConfirmBinding, PayConfirmViewModel>() {
    val simpleDateFormat = SimpleDateFormat(SIMPLE_DATE_FORMAT)
    lateinit var adapter: ItemConfirmAdapter
    var items: MutableList<DetailItemChoose> = mutableListOf()
    lateinit var billResponse: BillResponse
    override fun getContentLayout(): Int {
        return R.layout.activity_pay_confirm
    }

    override fun initView() {
        paddingStatusBar(binding.root)
        billResponse =
            Gson().fromJson(arguments?.getString(BILL_RESPONSE), BillResponse::class.java)
        binding.tvName.text = MyPreference().getInstance(binding.root.context)?.getUser()?.fullname
        binding.tvTime.text = simpleDateFormat.format(Calendar.getInstance().time)
        items = arguments?.getParcelableArrayList(ITEMS_PICKED)!!
        binding.tvPromotionItem.text = billResponse.promotion.toString() + "%"
        val priceDiscount =
            (billResponse.promotion.div(100f)).times(billResponse.totalPrice!!).toInt()
        binding.tvPriceDiscount.text =
            priceDiscount.formatToPrice()
        adapter = ItemConfirmAdapter(items)
        binding.rcItem.layoutManager = LinearLayoutManager(binding.root.context)
        binding.rcItem.adapter = adapter
        binding.tvPrice.text = billResponse.totalPrice!!.minus(priceDiscount).formatToPrice()
    }

    override fun initListener() {
        binding.tvCreate.setOnClickListener {
            val priceDiscount =
                (billResponse.promotion.div(100f)).times(billResponse.totalPrice!!).toInt()
            viewModel.confirmBill(
                billResponse.id ?: 0,
                OrderStatus.COMPLETED.ordinal,
                billResponse.totalPrice!!.minus(priceDiscount),
            )
        }
        binding.tvCancel.setOnClickListener {
            viewModel.confirmBill(
                billResponse.id ?: 0,
                OrderStatus.CANCELLED.ordinal,
                billResponse.totalPrice!!,
            )
        }
    }

    override fun observerLiveData() {
        viewModel.message.observe(this) {
            viewModel.repository.resetData()
            Toast.makeText(binding.root.context, it, Toast.LENGTH_LONG).show()
            // loadingDialog?.hide()
            onSuccess.invoke()
        }
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(binding.root.context),
        )[PayConfirmViewModel::class.java]
    }
}
