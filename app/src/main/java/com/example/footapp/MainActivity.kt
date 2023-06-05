package com.example.footapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.footapp.base.BaseActivity
import com.example.footapp.databinding.ActivityMainBinding
import com.example.footapp.ui.Order.HomeFragment
import com.example.footapp.ui.Order.OrderViewModel
import com.example.footapp.ui.manage.AccountFragment
import com.example.footapp.ui.orderlist.OrderListFragment
import com.example.footapp.ui.pay.PayConfirmFragment
import com.example.footapp.ui.statistic.StatisticFragment
import com.example.footapp.utils.*

class MainActivity : BaseActivity<ActivityMainBinding, OrderViewModel>() {

    lateinit var currentFragment: Fragment
    private lateinit var targetFragment: Fragment
    private val homeFragment by lazy {
        initializeFragment(HOME_FRAGMENT_TAG) {
            HomeFragment {
                showPayConfirmFragment(
                    it,
                )
            }
        }
    }
    private val orderListFragment by lazy { initializeFragment(ORDER_LIST_FRAGMENT) { OrderListFragment() } }
    private val payConfirmFragment by lazy { initializeFragment(PAY_CONFIRM_FRAGMENT) { PayConfirmFragment { onSuccessInPayConfirmFragment() } } }
    private val accountFragment by lazy { initializeFragment(ACCOUNT_FRAGMENT) { AccountFragment() } }
    private val statisticFragment by lazy { initializeFragment(STATISTIC_FRAGMENT) { StatisticFragment() } }

    override fun observerData() {
    }

    override fun getContentLayout(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        setColorForStatusBar(R.color.colorPrimary)
        setLightIconStatusBar(true)
        supportFragmentManager
            .beginTransaction()
            .show(homeFragment)
            .commit()
    }

    override fun initListener() {
        binding.viewOrder.setOnClickListener {
//            targetFragment = orderListFragment
//            currentFragment =
//                supportFragmentManager.fragments.firstOrNull { !it.isHidden } ?: homeFragment
//            showFragment(currentFragment, targetFragment)
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainer, orderListFragment)
                .commit()
        }
        binding.viewHome.setOnClickListener {
//            targetFragment = homeFragment
//            currentFragment =
//                supportFragmentManager.fragments.firstOrNull { !it.isHidden } ?: homeFragment
//            showFragment(currentFragment, targetFragment)
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainer, homeFragment)
                .commit()
        }
        binding.viewAccount.setOnClickListener {
//            targetFragment = accountFragment
//            currentFragment =
//                supportFragmentManager.fragments.firstOrNull { !it.isHidden } ?: homeFragment
//            showFragment(currentFragment, targetFragment)
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainer, accountFragment)
                .commit()
        }
        binding.viewStatistic.setOnClickListener {
//            targetFragment = statisticFragment
//            currentFragment =
//                supportFragmentManager.fragments.firstOrNull { !it.isHidden } ?: homeFragment
//            showFragment(currentFragment, targetFragment)
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainer, statisticFragment)
                .commit()
        }
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(this, ViewModelFactory(this))[OrderViewModel::class.java]
    }

    private fun initializeFragment(tag: String, createFragment: () -> Fragment): Fragment {
        return supportFragmentManager.findFragmentByTag(tag) ?: createFragment().also { fragment ->
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragmentContainer, fragment, tag)
//                .hide(fragment)
                .commit()
        }
    }

    private fun showFragment(currentFragment: Fragment, targetFragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, targetFragment)
            .commit()
        hideSoftKeyboard()
        loadingDialog?.hide()
    }

    private fun showPayConfirmFragment(bundle: Bundle) {
        currentFragment =
                supportFragmentManager.fragments.firstOrNull { !it.isHidden } ?: homeFragment
            //showFragment(currentFragment, targetFragment)
        payConfirmFragment.arguments = bundle
        showFragment(currentFragment, payConfirmFragment)
    }

    private fun onSuccessInPayConfirmFragment() {
        currentFragment =
            supportFragmentManager.fragments.firstOrNull { !it.isHidden } ?: homeFragment
        showFragment(currentFragment, homeFragment)
    }
}
