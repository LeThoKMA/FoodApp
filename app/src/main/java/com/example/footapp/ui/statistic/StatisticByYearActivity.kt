package com.example.footapp.ui.statistic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.core.content.ContextCompat
import com.example.footapp.R
import com.example.footapp.databinding.ActivityStatisticByYearBinding
import com.example.footapp.ui.BaseActivity
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class StatisticByYearActivity : BaseActivity<ActivityStatisticByYearBinding>() {
    lateinit var presenter: StatsticPresenter
     var total=0

    override fun getContentLayout(): Int {
        return R.layout.activity_statistic_by_year
    }


    override fun initView() {
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
        var i = 0
        months.add("")
        for (item in map) {
//            list.add(BarEntry(item.value.toFloat(), item.value.toFloat()))
            //   list.add(BarEntry(i.toFloat(), item.value.toFloat()))
            total+=item.value

            i++
            months.add("T" + item.key)
        }
       // list.add(BarEntry(0f, 100f))
        list.add(BarEntry(1f, 100f))
        list.add(BarEntry(2f, 120f))
        list.add(BarEntry(3f, 150f))
        list.add(BarEntry(4f, 111f))
        list.add(BarEntry(5f, 222f))
        list.add(BarEntry(6f, 333f))
        list.add(BarEntry(7f, 100f))
        list.add(BarEntry(8f, 222f))
        list.add(BarEntry(9f, 100f))
        list.add(BarEntry(10f, 222f))
        list.add(BarEntry(11f, 100f))
        list.add(BarEntry(12f, 222f))

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