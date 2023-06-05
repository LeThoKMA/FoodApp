//package com.example.footapp.ui.statistic
//
//import android.view.View
//import android.widget.AdapterView
//import android.widget.AdapterView.OnItemSelectedListener
//import androidx.core.content.ContextCompat
//import com.example.footapp.R
//import com.example.footapp.base.BaseActivity
//import com.example.footapp.databinding.ActivityStatisticBinding
//import com.example.footapp.ui.Order.OrderViewModel
//import com.github.mikephil.charting.components.Legend
//import com.github.mikephil.charting.data.Entry
//import com.github.mikephil.charting.data.LineData
//import com.github.mikephil.charting.data.LineDataSet
//import java.util.*
//
//class StatisticByMonthActivity : BaseActivity<ActivityStatisticBinding, OrderViewModel>() {
//    // lateinit var presenter: StatsticPresenter
//    var types: ArrayList<String> = arrayListOf()
//    lateinit var adapter: SpinnerAdapter
//    var list: ArrayList<Entry> = arrayListOf()
//    override fun getContentLayout(): Int {
//        return R.layout.activity_statistic
//    }
//
//    override fun initView() {
//        setColorForStatusBar(R.color.colorPrimary)
//        setLightIconStatusBar(false)
//        //  presenter = StatsticPresenter(this)
//        //  presenter.getBills()
//        for (i in 1 until 13) {
//            types.add("Tháng $i")
//        }
//        adapter = SpinnerAdapter(types, this)
//        binding.spinner.adapter = adapter
//        binding.spinner.setSelection(Calendar.getInstance().get(Calendar.MONTH))
//
////        presenter.dataInMonth.observe(this@StatisticByMonthActivity) {
////            if (it != null) {
////                binding.chart.visibility = View.VISIBLE
////                binding.tvLabel.visibility = View.VISIBLE
////                setUpViewChart(it)
////                loadingDialog?.dismiss()
////            }
////        }
//    }
//
//    override fun initListener() {
//        binding.spinner.onItemSelectedListener = object : OnItemSelectedListener {
//            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
////                presenter.dataCheck.observe(this@StatisticByMonthActivity) {
////                    if (it) {
////                        loadingDialog?.show()
////                        val year = Calendar.getInstance().get(Calendar.YEAR)
////
////                        presenter.getDataInMonth("${p2.plus(1)}-$year")
////                    }
////                }
//            }
//
//            override fun onNothingSelected(p0: AdapterView<*>?) {
//            }
//        }
//        binding.imvBack.setOnClickListener { finish() }
//    }
//
//    fun setUpViewChart(map: HashMap<String, Int>) {
//        val list: ArrayList<Entry> = arrayListOf()
//        var i = 0
//        for (item in map.values.reversed()) {
//            list.add(Entry(i.toFloat(), item.toFloat()))
//            i++
//        }
//        val lineDataSet = LineDataSet(list, "")
//        lineDataSet.color = (ContextCompat.getColor(this, R.color.colorPrimary))
//
//        val lineData = LineData(lineDataSet)
//        binding.chart.xAxis.labelCount = list.size
//
//        binding.chart.xAxis.isEnabled = false
//        binding.chart.description.text = ""
//        binding.chart.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
//
//        // chart.xAxis.valueFormatter=IndexAxisValueFormatter()
//        binding.chart.legend.isEnabled = false
//        binding.chart.data = lineData
//        binding.chart.invalidate()
//    }
//
//    override fun observerData() {
//        TODO("Not yet implemented")
//    }
//
//    override fun initViewModel() {
//        TODO("Not yet implemented")
//    }
//}
