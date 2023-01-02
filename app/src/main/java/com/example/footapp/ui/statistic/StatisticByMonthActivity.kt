package com.example.footapp.ui.statistic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.core.content.ContextCompat
import com.example.footapp.R
import com.example.footapp.databinding.ActivityStatisticBinding
import com.example.footapp.ui.BaseActivity
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import java.time.Month
import java.util.Calendar

class StatisticByMonthActivity : BaseActivity<ActivityStatisticBinding>() {
    lateinit var presenter: StatsticPresenter
    var types: ArrayList<String> = arrayListOf()
    lateinit var adapter: SpinnerAdapter
    var list: ArrayList<Entry> = arrayListOf()
    override fun getContentLayout(): Int {
        return R.layout.activity_statistic
    }

    override fun initView() {
        presenter = StatsticPresenter(this)
        //  presenter.getBills()
        for (i in 1 until 13) {
            types.add("Tháng $i")
        }
        adapter = SpinnerAdapter(types, this)
        binding.spinner.adapter = adapter
        binding.spinner.setSelection(Calendar.getInstance().get(Calendar.MONTH))

        presenter.dataInMonth.observe(this@StatisticByMonthActivity)
        {
            if (it != null) {

                setUpViewChart(it)
                loadingDialog?.dismiss()
            }
        }
    }

    override fun initListener() {
        binding.spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                presenter.dataCheck.observe(this@StatisticByMonthActivity)
                {
                    if (it) {
                        loadingDialog?.show()
                        presenter.getDataInMonth("12-2022")
                        //  presenter.getDataInMonth("${p2.plus(1)}-2022)")

                    }
                }

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }
    }

    fun setUpViewChart(map: HashMap<String, Int>) {
        var list: ArrayList<Entry> = arrayListOf()
        var i = 0
        for (item in map) {
            list.add(Entry(i.toFloat(), item.value.toFloat()))
            i++
        }
        var lineDataSet = LineDataSet(list,"")

        var lineData = LineData(lineDataSet)
        binding.chart.xAxis.labelCount = list.size

        binding.chart.xAxis.isEnabled = false
        binding.chart.description.text = ""
        binding.chart.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT

        //chart.xAxis.valueFormatter=IndexAxisValueFormatter()
        binding.chart.data = lineData
        binding.chart.invalidate()
    }
}