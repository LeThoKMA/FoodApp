package com.example.footapp.base

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Resources
import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.TypedValue
import android.view.*
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.footapp.R
import com.example.footapp.ui.Order.OrderWhenNetworkErrorActivity
import com.example.footapp.ui.Order.OrderWhenNetworkErrorViewModel
import com.example.footapp.ui.login.LoginActivity
import com.google.android.material.snackbar.Snackbar

abstract class BaseActivity<BINDING : ViewDataBinding, VM : BaseViewModel> :
    AppCompatActivity() {
    private val connectivityManager by lazy { getSystemService(ConnectivityManager::class.java) }
    lateinit var binding: BINDING
    lateinit var viewModel: VM
    var loadingDialog: AlertDialog? = null
    private var snackBar: Snackbar? = null
    private var mLastClickTime: Long = 0

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        binding = DataBindingUtil.setContentView(this, getContentLayout())
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        loadingDialog = setupProgressDialog()
        initViewModel()
        observerDefaultLiveData()
        initView()
        initListener()
        observerData()
        networkStateListener(this)
    }


    abstract fun observerData()

    abstract fun getContentLayout(): Int

    abstract fun initView()

    abstract fun initListener()

    abstract fun initViewModel()

    open fun isDoubleClick(): Boolean {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return true
        }
        mLastClickTime = SystemClock.elapsedRealtime()
        return false
    }

    private fun observerDefaultLiveData() {
        viewModel.apply {
            isLoading.observe(this@BaseActivity) {
                if (it) {
                    loadingDialog?.show()
                } else {
                    loadingDialog?.dismiss()
                }
            }
            errorMessage.observe(this@BaseActivity) {
                if (it != null) {
                    showError(it.toInt())
                }
            }
            responseMessage.observe(this@BaseActivity) {
                showError(it.toString())
            }
        }
    }

    private fun showError(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
//        val errorSnackbar = Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_LONG)
//        errorSnackbar.setAction("", null)
//        errorSnackbar.show()
    }

    private fun showError(@StringRes id: Int) {
        val errorSnackbar = Snackbar.make(binding.root, id, Snackbar.LENGTH_LONG)
        errorSnackbar.setAction("", null)
        errorSnackbar.show()
    }

    private fun setupProgressDialog(): AlertDialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this, R.style.CustomDialog)
        builder.setCancelable(false)

        val myLayout = LayoutInflater.from(this)
        val dialogView: View = myLayout.inflate(R.layout.fragment_progress_dialog, null)

        builder.setView(dialogView)

        val dialog: AlertDialog = builder.create()
        val window: Window? = dialog.window
        if (window != null) {
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog.window?.attributes)
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
            dialog.window?.attributes = layoutParams
        }
        return dialog
    }

    private fun networkStateListener(context: Context) {
        connectivityManager.registerDefaultNetworkCallback(object :
            ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                if (context is OrderWhenNetworkErrorActivity) {
                    showSnackBarNetworkIsBack()
                }
            }

            override fun onLost(network: Network) {
                if (context !is OrderWhenNetworkErrorActivity) {
                    showSnackBarLostNetwork()
                }
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
            }

            override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
            }
        })
    }

    fun transparentStatusbar() {
        window.statusBarColor = 0x00000000
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    fun setColorForStatusBar(color: Int) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, color)
    }

    fun setLightIconStatusBar(isLight: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (isLight) {
                window.decorView.windowInsetsController?.setSystemBarsAppearance(
                    0,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                )
            } else {
                window.decorView.windowInsetsController?.setSystemBarsAppearance(
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                )
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val window = window
            val decorView = window.decorView
            val wic = WindowInsetsControllerCompat(window, decorView)
            wic.isAppearanceLightStatusBars = isLight // true or false as desired.

            // And then you can set any background color to the status bar.
//                window.statusBarColor = Color.WHITE
        }
    }

    @SuppressLint("ResourceAsColor")
    fun showSnackBarLostNetwork() {
        snackBar = Snackbar.make(
            binding.root,
            "Đã mất kết nối mạng, chuyển sang chế độ offline",
            Snackbar.LENGTH_INDEFINITE,
        )
        val snackBarText =
            snackBar?.view?.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)

        val textSizeInSp = 18f // Kích thước chữ mong muốn (theo sp)

        val layoutParams = snackBar?.view?.layoutParams as FrameLayout.LayoutParams
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL
        layoutParams.topMargin = 75
        snackBar!!.view.layoutParams = layoutParams
        snackBar!!.view.setBackgroundColor(R.color.white)
        snackBarText?.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSizeInSp)
        snackBar!!.show()

        snackBar!!.view.setOnClickListener {
            startActivity(Intent(this, OrderWhenNetworkErrorActivity::class.java))
        }
    }

    fun showSnackBarNetworkIsBack(){
        snackBar = Snackbar.make(
            binding.root,
            "Đã có kết nối mạng, quay trở lại chế độ online",
            Snackbar.LENGTH_INDEFINITE,
        )
        val snackBarText =
            snackBar?.view?.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)

        val textSizeInSp = 18f // Kích thước chữ mong muốn (theo sp)

        val layoutParams = snackBar?.view?.layoutParams as FrameLayout.LayoutParams
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL
        layoutParams.topMargin = 75
        snackBar!!.view.layoutParams = layoutParams
        snackBar!!.view.setBackgroundColor(R.color.white)
        snackBarText?.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSizeInSp)
        snackBar!!.show()

        snackBar!!.view.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }

    override fun onDestroy() {
        loadingDialog?.dismiss()
        super.onDestroy()
    }
}
