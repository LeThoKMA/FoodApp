package com.example.footapp.ui.pay

import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.footapp.MyPreference
import com.example.footapp.R
import com.example.footapp.ViewModelFactory
import com.example.footapp.databinding.ActivityPayConfirmBinding
import com.example.footapp.interface1.PayConfirmInterface
import com.example.footapp.model.BillResponse
import com.example.footapp.model.DetailItemChoose
import com.example.footapp.presenter.OrderViewModel
import com.example.footapp.presenter.PayConfirmViewModel
import com.example.footapp.ui.BaseActivity
import com.example.footapp.utils.*
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*

class PayConfirmActivity :
    BaseActivity<ActivityPayConfirmBinding, PayConfirmViewModel>(),
    PayConfirmInterface {
    lateinit var adapter: ItemConfirmAdapter
    var items: MutableList<DetailItemChoose> = mutableListOf()
    lateinit var billResponse: BillResponse
    override fun getContentLayout(): Int {
        return R.layout.activity_pay_confirm
    }

    override fun initView() {
        setColorForStatusBar(R.color.colorPrimary)
        setLightIconStatusBar(false)

        billResponse = intent.getSerializableExtra(BILL_RESPONSE) as BillResponse
        binding.tvName.text = MyPreference().getInstance(this)?.getUser()?.name
        //binding.tvTime.text = simpleDateFormat.format(Calendar.getInstance().time)
        items = Gson().fromJson<MutableList<DetailItemChoose>>(intent.getStringExtra(ITEMS_PICKED),MutableList::class.java)
        adapter = ItemConfirmAdapter(items)
        binding.rcItem.layoutManager = LinearLayoutManager(this)
        binding.rcItem.adapter = adapter
        binding.tvPrice.text = intent.getIntExtra(TOTAL_PRICE, 0).toString() + " đ"
    }

    override fun initListener() {
        binding.tvCreate.setOnClickListener { showDialog() }
        binding.imvBack.setOnClickListener {
            finish()
        }
    }

    fun showDialog() {
        var dialog = ConfirmDialog(object : ConfirmDialog.CallBack {
            override fun accept(passwd: String) {
            //    presenter.payConfirm(map, passwd, intent.getIntExtra(TOTAL_PRICE, 0))
            }
        })
        dialog.show(supportFragmentManager, "")
    }

    override fun complete(message: String, flag: Boolean) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        if (flag) {
            var intent = Intent(TABLE_ACTION)
            sendBroadcast(intent)
            finish()
        }
    }

    override fun observerData() {
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(this, ViewModelFactory(this))[PayConfirmViewModel::class.java]
    }
}
