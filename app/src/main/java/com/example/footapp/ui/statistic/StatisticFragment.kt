package com.example.footapp.ui.statistic

import android.animation.ValueAnimator
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.footapp.R
import com.example.footapp.ViewModelFactory
import com.example.footapp.base.BaseFragment
import com.example.footapp.databinding.ActivityStatisticByYearBinding
import com.example.footapp.model.ItemStatistic
import com.example.footapp.utils.formatToPrice
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class StatisticFragment : BaseFragment<ActivityStatisticByYearBinding, StatisticViewModel>() {
    var total = 0
    lateinit var spinnerAdapter: SpinnerAdapter
    val years = mutableListOf<Int>()

    override fun getContentLayout(): Int {
        return R.layout.activity_statistic_by_year
    }

    override fun initView() {
        paddingStatusBar(binding.root)
    }

    override fun initListener() {
        binding.spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                viewModel.getStatisticOfYear(years[p2])
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    fun setUpViewChart(data: List<ItemStatistic>) {
        val list: ArrayList<BarEntry> = arrayListOf()
        val months: ArrayList<String> = arrayListOf()
        var i = 1
        months.add("")
        for (item in data) {
            list.add(BarEntry(i.toFloat(), item.revenue!!.toFloat()))
            total += item.revenue!!

            i++
            months.add(item.time.toString())
        }

        val barDataSet = BarDataSet(list, "")
        barDataSet.color = (ContextCompat.getColor(binding.root.context, R.color.colorPrimary))
        val barData = BarData(barDataSet)

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
        // binding.barchart.xAxis.setCenterAxisLabels(true)
        binding.barchart.xAxis.setDrawGridLines(false)
        binding.barchart.setMaxVisibleValueCount(5)
        binding.barchart.invalidate()

        binding.tvTotal.text = total.formatToPrice()
    }

    override fun observerLiveData() {
        viewModel.dataInYear.observe(this) {
            if (it != null) {
                Log.e("TAG", it.toString())
                binding.barchart.visibility = View.VISIBLE
                setUpViewChart(it)
                loadingDialog?.dismiss()
            }
        }
        viewModel.year.observe(this) {
            years.addAll(it)
            spinnerAdapter = SpinnerAdapter(years, binding.root.context)
            binding.spinner.adapter = spinnerAdapter
        }
        viewModel.dataInDay.observe(this) {
            it?.let {
                val animator = ValueAnimator.ofInt(it.revenue?.div(2) ?: 0, it.revenue!!)
                animator.duration = 4000
                animator.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener {
                    override fun onAnimationUpdate(p0: ValueAnimator) {
                        binding.tvToday.text = (p0.animatedValue as Int).formatToPrice()
                    }
                })
                animator.start()
            }
        }
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(binding.root.context),
        )[StatisticViewModel::class.java]
    }
}
