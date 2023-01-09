package com.example.footapp.ui.history

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.footapp.R
import com.example.footapp.databinding.ActivityHistoryBinding
import com.example.footapp.model.Bill
import com.example.footapp.ui.BaseActivity

class HistoryActivity :BaseActivity<ActivityHistoryBinding>() {

    lateinit var adapter: HistoryAdapter
    var list:ArrayList<Bill> = arrayListOf()
    var presenter=HistoryPresenter()
    override fun getContentLayout(): Int {
        return R.layout.activity_history
    }

    override fun initView() {
        setColorForStatusBar(R.color.colorPrimary)
        setLightIconStatusBar(false)
        loadingDialog?.show()
        adapter= HistoryAdapter(list)
        binding.rcBill.layoutManager=LinearLayoutManager(this)
        binding.rcBill.adapter=adapter
        presenter.bill.observe(this)
        {
            if(it!=null)
            {
                list.clear()
                list.addAll(it)
                adapter.notifyDataSetChanged()
                loadingDialog?.dismiss()
            }
        }
    }

    override fun initListener() {
        binding.imvBack.setOnClickListener { finish() }
    }
}