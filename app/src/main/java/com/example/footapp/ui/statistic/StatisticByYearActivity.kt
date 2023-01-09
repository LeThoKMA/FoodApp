package com.example.footapp.ui.statistic

import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.example.footapp.R
import com.example.footapp.databinding.ActivityStatisticByYearBinding
import com.example.footapp.presenter.StatsticPresenter
import com.example.footapp.ui.BaseActivity
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class StatisticByYearActivity : BaseActivity<ActivityStatisticByYearBinding>() {
    lateinit var presenter: StatsticPresenter
     var total=0

    override fun getContentLayout(): Int {
        return R.layout.activity_statistic_by_year
    }


    override fun initView() {
        setColorForStatusBar(R.color.colorPrimary)
        setLightIconStatusBar(false)
        presenter = StatsticPresenter(this)
        loadingDialog?.show()

        presenter.dataCheck.observe(this)
        {
            if (it) {
                presenter.getDataInYear()
            }
        }


        presenter.dataInYear.observe(this)
        {
            if (it != null) {

                Log.e("TAG", it.toString())
                binding.barchart.visibility=View.VISIBLE
                binding.textView4.visibility=View.VISIBLE
                setUpViewChart(it)
                loadingDialog?.dismiss()
            }
        }
    }

    override fun initListener() {
        binding.imvBack.setOnClickListener { finish() }
    }

    fun setUpViewChart(map: HashMap<Int, Int>) {
        var list: ArrayList<BarEntry> = arrayListOf()
        var months: ArrayList<String> = arrayListOf()
        var i = 1
        months.add("")
        for (item in map) {

               list.add(BarEntry(i.toFloat(), item.value.toFloat()))
            total+=item.value

            i++
            months.add("T" + item.key)
        }


        var barDataSet = BarDataSet(list, "")
        barDataSet.color = (ContextCompat.getColor(this, R.color.colorPrimary))
        var barData = BarData(barDataSet)

        binding.barchart.data = barData

        binding.barchart.xAxis.labelCount = list.size + 1
        binding.barchart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.barchart.axisRight.setDrawLabels(false)
        binding.barchart.legend.isEnabled = false
        binding.barchart.xAxis.granularity = 1f
        binding.barchart.data.barWidth = 0.8f
//        binding.barchart.xAxis.valueFormatter = object : ValueFormatter() {
//            override fun getFormattedValue(value: Float): String {
//                return if (value >= 0 && value < months.size) {
//                    Log.e("TAG11111", value.toString() )
//                    months[value.toInt()]
//
//                } else {
//                    ""
//                }
//            }
//
//        }
        binding.barchart.xAxis.valueFormatter = IndexAxisValueFormatter(months)

        binding.barchart.description.text = ""
        binding.barchart.axisRight.setDrawAxisLine(false)
        binding.barchart.xAxis.axisMinimum = 0f


        // binding.barchart.xAxis.axisMaximum= (list.size).toFloat()
        //binding.barchart.xAxis.setCenterAxisLabels(true)
        binding.barchart.xAxis.setDrawGridLines(false);
        binding.barchart.setMaxVisibleValueCount(5)
        binding.barchart.invalidate()

        binding.tvTotal.text=total.toString()+" đồng"
    }

}