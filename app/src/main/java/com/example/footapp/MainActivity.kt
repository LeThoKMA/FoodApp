package com.example.footapp

import android.app.ActivityOptions
import android.content.Intent
import android.hardware.display.DisplayManager
import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.NavHostFragment
import com.example.footapp.base.BaseActivity
import com.example.footapp.databinding.ActivityMainBinding
import com.example.footapp.network.SocketIoManage
import com.example.footapp.ui.customer.CustomerActivity
import com.example.footapp.ui.orderlist.OrderListFragment
import com.example.footapp.utils.ORDER_LIST_FRAGMENT
import com.example.footapp.utils.hideSoftKeyboard
import kotlinx.coroutines.launch

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {
    private var countOrder = 0
    private lateinit var navHostFragment: NavHostFragment

    override fun observerData() {
    }

    override fun getContentLayout(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        setColorForStatusBar(R.color.colorPrimary)
        setLightIconStatusBar(true)
        showScreenCustomer()

        navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainer) as NavHostFragment? ?: return

        SocketIoManage.subcribe()
        SocketIoManage.mSocket?.on("bill") { args ->
            viewModel.viewModelScope.launch {
                countOrder++
                binding.orderCount.text = countOrder.toString()
                binding.orderCount.visibility = View.VISIBLE
                val fragment = supportFragmentManager.findFragmentByTag(ORDER_LIST_FRAGMENT)
                if (fragment is OrderListFragment) {
                    fragment.showSnackBar()
                }
            }
        }
    }


    override fun initListener() {
        binding.viewOrder.setOnClickListener {
            countOrder = 0
            binding.orderCount.visibility = View.GONE
            navigateToDest(R.id.orderList_dest)
        }
        binding.viewHome.setOnClickListener {
            navigateToDest(R.id.home_dest)
        }
        binding.viewAccount.setOnClickListener {
            navigateToDest(R.id.account_dest)
        }
        binding.viewStatistic.setOnClickListener {
            navigateToDest(R.id.statistic_dest)
        }
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(this, ViewModelFactory())[MainViewModel::class.java]
    }

    private fun initializeFragment(tag: String, createFragment: () -> Fragment): Fragment {
        return supportFragmentManager.findFragmentByTag(tag) ?: createFragment().also { fragment ->
            supportFragmentManager
                .beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.fragmentContainer, fragment, tag)
                .commit()
        }
    }

    private fun showFragment(targetFragment: Fragment) {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)

        if (currentFragment != null && currentFragment.javaClass == targetFragment.javaClass) {
            // Fragment đã hiển thị, không cần thay thế
            return
        }
        currentFragment?.let {
            supportFragmentManager
                .beginTransaction()
                .hide(it)
                .show(targetFragment)
                .commit()
        }
        hideSoftKeyboard()
        loadingDialog?.hide()
    }

    private fun showScreenCustomer() {
        val displayManager = getSystemService(DISPLAY_SERVICE) as DisplayManager
        val displays = displayManager.displays
        if (displays.size > 1) {
            // Activity options are used to select the display screen.
            val options = ActivityOptions.makeBasic()

            // Select the display screen that you want to show the second activity
            options.launchDisplayId = displays[1].displayId
            // To display on the second screen that your intent must be set flag to make
            // single task (combine FLAG_ACTIVITY_CLEAR_TOP and FLAG_ACTIVITY_NEW_TASK)
            // or you also set it in the manifest (see more at the manifest file)
            startActivity(
                Intent(this, CustomerActivity::class.java),
                options.toBundle(),
            )
        }
    }

    private fun navigateToDest(id: Int) {
        val navController = navHostFragment.navController
        val currentDestination = navController.currentDestination

        // Kiểm tra xem điểm đến đã được hiển thị hay chưa
        if (currentDestination?.id != id) {
            navController.navigate(id)
        }
    }
}
