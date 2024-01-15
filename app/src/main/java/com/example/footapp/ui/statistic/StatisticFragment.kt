package com.example.footapp.ui.statistic

import android.animation.ValueAnimator
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.footapp.R
import com.example.footapp.ViewModelFactory
import com.example.footapp.base.BaseFragment
import com.example.footapp.databinding.ActivityStatisticByYearBinding
import com.example.footapp.ui.history.StatisticStaffAdapter
import com.example.footapp.ui.statistic.component.BarCharView
import com.example.footapp.utils.formatToPrice
import kotlinx.coroutines.launch

class StatisticFragment : BaseFragment<ActivityStatisticByYearBinding, StatisticViewModel>() {
    var spinnerAdapter: SpinnerAdapter? = null
    var spinnerTimeUser: SpinnerAdapter? = null
    var staffAdapter: StatisticStaffAdapter? = null
    var spinnerList = mutableListOf<String>()
    override fun getContentLayout(): Int {
        return R.layout.activity_statistic_by_year
    }

    override fun initView() {
        val binding = binding!!
        paddingStatusBar(binding.root)
        staffAdapter = StatisticStaffAdapter(mutableListOf())
        binding.rcStatisticUser.layoutManager = LinearLayoutManager(requireContext())
        binding.rcStatisticUser.adapter = staffAdapter
        spinnerAdapter = SpinnerAdapter(spinnerList, requireContext())
        binding.spinner.adapter = spinnerAdapter
    }

    override fun initListener() {
        val binding = binding!!
        binding.spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                viewModel.doGetStatisticInYear(viewModel.year.value?.get(p2) ?: 0)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        binding.spinnerTimeUser.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                viewModel.getStatisticStaff(spinnerTimeUser?.getItem(p2).toString())
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    override fun observerLiveData() {
        viewModel.year.observe(viewLifecycleOwner) {
            spinnerList.clear()
            spinnerList.addAll(it.map { it.toString() })
            spinnerAdapter?.notifyDataSetChanged()
        }
        viewModel.dataInDay.observe(this) {
            it?.let {
                val animator = ValueAnimator.ofInt(it.revenue.div(2) ?: 0, it.revenue)
                animator.duration = 4000
                animator.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener {
                    override fun onAnimationUpdate(p0: ValueAnimator) {
                        binding?.tvToday?.text = (p0.animatedValue as Int).formatToPrice()
                    }
                })
                animator.start()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stateNormal.collect {
                    when (it) {
                        is StatisticViewModel.StateNormal.StaffTime -> {
                            spinnerTimeUser = SpinnerAdapter(it.staffTime, requireContext())
                            binding?.spinnerTimeUser?.adapter = spinnerTimeUser
                        }

                        is StatisticViewModel.StateNormal.StaffDataForStatistic -> {
                            staffAdapter?.setData(it.staffList)
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    binding?.barchart?.setContent {
                        BarCharView(it.stoneValue, it.barDatas.toMutableList(), it.isShowBarChart)
                    }
                }
            }
        }
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(requireContext()),
        )[StatisticViewModel::class.java]
    }

    override fun onDestroyView() {
        super.onDestroyView()
        spinnerAdapter = null
        staffAdapter = null
        spinnerTimeUser = null
    }
}
