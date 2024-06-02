package com.example.footapp.ui.Order.offline

import android.os.Build
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.footapp.MyPreference
import com.example.footapp.R
import com.example.footapp.ViewModelFactory
import com.example.footapp.base.BaseActivity
import com.example.footapp.databinding.ActivityPayConfirmBinding
import com.example.footapp.model.DetailItemChoose
import com.example.footapp.model.ItemBill
import com.example.footapp.ui.pay.ItemConfirmAdapter
import com.example.footapp.utils.ITEMS_PICKED
import com.example.footapp.utils.SIMPLE_DATE_FORMAT
import com.example.footapp.utils.TOTAL_PRICE
import com.example.footapp.utils.formatToPrice
import com.example.footapp.utils.toast
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar

class OfflineConfirmActivity : BaseActivity<ActivityPayConfirmBinding, OfflineConfirmViewModel>() {
    private val simpleDateFormat = SimpleDateFormat(SIMPLE_DATE_FORMAT)
    lateinit var adapter: ItemConfirmAdapter
    private val items by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableArrayListExtra(ITEMS_PICKED, DetailItemChoose::class.java)
        } else {
            intent.getParcelableArrayListExtra(ITEMS_PICKED)
        }
    }
    private val totalPrice by lazy { intent.getIntExtra(TOTAL_PRICE, 0) }
    private val staff by lazy { MyPreference().getInstance(this)?.getUser() }

    override fun observerData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect(::handleState)
            }
        }
    }

    private fun handleState(state: OfflineConfirmViewModel.UiState) {
        when (state) {
            is OfflineConfirmViewModel.UiState.Message -> {
                toast(state.message.toString())
            }

            is OfflineConfirmViewModel.UiState.SuccessInsert -> {
                toast(state.message.toString())
                finish()
            }
        }
    }

    override fun getContentLayout(): Int {
        return R.layout.activity_pay_confirm
    }

    override fun initView() {
        binding.tvName.text = staff?.fullname
        binding.tvTime.text = simpleDateFormat.format(Calendar.getInstance().time)
        adapter = ItemConfirmAdapter(items ?: mutableListOf())
        binding.rcItem.layoutManager = LinearLayoutManager(this)
        binding.rcItem.adapter = adapter
        binding.tvPrice.text = totalPrice.formatToPrice()
    }

    override fun initListener() {
        binding.tvCreate.setOnClickListener {
            items?.map {
                ItemBill(
                    productId = it.id,
                    count = it.count,
                    totalPrice = it.totalPrice,
                    staffId = staff?.id,
                )
            }
                ?.let { it1 -> viewModel.insertBill(it1) }
        }
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(),
        )[OfflineConfirmViewModel::class.java]
    }
}
