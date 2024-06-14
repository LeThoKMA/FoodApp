package com.example.footapp.ui.pay

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
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

class PayConfirmFragment() :
    BaseFragment<ActivityPayConfirmBinding, PayConfirmViewModel>() {
    private val simpleDateFormat = SimpleDateFormat(SIMPLE_DATE_FORMAT)
    private val mainViewModel: MainViewModel by activityViewModels()
    lateinit var adapter: ItemConfirmAdapter
    private var items: MutableList<DetailItemChoose> = mutableListOf()
    lateinit var billResponse: BillResponse

    override fun getContentLayout(): Int {
        return R.layout.activity_pay_confirm
    }

    @SuppressLint("SetTextI18n")
    override fun initView() {
        val binding = binding!!
        binding.let { paddingStatusBar(it.root) }
        billResponse =
            Gson().fromJson(
                mainViewModel.dataToPay.value?.getString(BILL_RESPONSE),
                BillResponse::class.java,
            )
        binding.tvName.text = MyPreference.getInstance().getUser().fullname
        binding.tvTime.text = simpleDateFormat.format(Calendar.getInstance().time)
        items = mainViewModel.dataToPay.value?.getParcelableArrayList(ITEMS_PICKED)!!
        binding.tvPromotionItem.text = billResponse.promotion.toString() + "%"
        val priceDiscount =
            (billResponse.promotion.div(100f)).times(billResponse.totalPrice!!).toInt()
        binding.tvPriceDiscount.text =
            priceDiscount.formatToPrice()
        adapter = ItemConfirmAdapter(items)
        binding.rcItem.layoutManager = LinearLayoutManager(requireContext())
        binding.rcItem.adapter = adapter
        binding.tvPrice.text = billResponse.totalPrice!!.minus(priceDiscount).formatToPrice()
    }

    override fun initListener() {
        val binding = binding!!
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
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            mainViewModel.onPaySuccess()
            findNavController().popBackStack(R.id.home_dest, false)
        }
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(),
        )[PayConfirmViewModel::class.java]
    }
}
