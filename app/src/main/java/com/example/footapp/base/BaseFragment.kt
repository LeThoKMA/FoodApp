package com.example.footapp.base

import android.content.Context
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.example.footapp.R
import com.example.footapp.utils.ANDROID
import com.example.footapp.utils.STATUS_BAR_HEIGHT
import com.google.android.material.snackbar.Snackbar

abstract class BaseFragment<BINDING : ViewDataBinding, VM : BaseViewModel> :
    Fragment() {

    lateinit var viewModel: VM

    lateinit var binding: BINDING
    var loadingDialog: AlertDialog? = null
    private var mLastClickTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadingDialog = setupProgressDialog()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            getContentLayout(),
            container,
            false,
        )
        return binding.root
    }

    open fun isDoubleClick(): Boolean {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return true
        }
        mLastClickTime = SystemClock.elapsedRealtime()
        return false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initView()
        initListener()
        observerLiveData()
        observerDefaultLiveData()
    }

    abstract fun getContentLayout(): Int

    abstract fun initViewModel()

    abstract fun initView()

    abstract fun initListener()

    abstract fun observerLiveData()

    private fun observerDefaultLiveData() {
        viewModel.apply {
            activity?.let {
                isLoading.observe(viewLifecycleOwner) {
                    if (it) {
                        loadingDialog?.show()
                    } else {
                        loadingDialog?.dismiss()
                    }
                }
            }
            activity?.let {
                errorMessage.observe(viewLifecycleOwner) {
                    if (it != null) {
                        showError(it.toInt())
                    }
                }
            }
            activity?.let {
                responseMessage.observe(viewLifecycleOwner) {
                    showError(it.toString())
                }
            }
        }
    }

    private fun showError(errorMessage: String) {
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
//        val errorSnackbar = Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_LONG)
//        errorSnackbar.setAction("", null)
//        errorSnackbar.show()
    }

    private fun showError(@StringRes id: Int) {
        val errorSnackbar = Snackbar.make(binding.root, id, Snackbar.LENGTH_LONG)
        errorSnackbar.setAction("", null)
        errorSnackbar.show()
    }

    private fun setupProgressDialog(): AlertDialog? {
        if (context != null) {
            val builder: AlertDialog.Builder =
                AlertDialog.Builder(requireContext(), R.style.CustomDialog)
            builder.setCancelable(false)

            val myLayout = LayoutInflater.from(requireContext())
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
        return null
    }

    protected fun paddingStatusBar(view: View) {
        view.setPadding(0, getStatusBarHeight(binding.root.context), 0, 0)
    }

    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId =
            context.resources.getIdentifier(STATUS_BAR_HEIGHT, "dp", ANDROID)
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }
}
