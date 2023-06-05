package com.example.footapp.ui.pay

import android.os.Bundle
import android.view.View
import com.example.footapp.base.BaseDialog
import com.example.footapp.R
import com.example.footapp.databinding.FilterDialogBinding

open class ConfirmDialog(var callback: CallBack) :
    BaseDialog<FilterDialogBinding>() {
    override fun getLayoutResource(): Int {
        return R.layout.filter_dialog
    }

    override fun init(saveInstanceState: Bundle?, view: View?) {

    }

    override fun setUp(view: View?) {
        binding.tvAcp.setOnClickListener {
            callback.accept(binding.edtFilter.text.toString())
            dismiss()

        }
        binding.tvCancel.setOnClickListener {
            dismiss()
        }

    }

    interface CallBack {
        fun accept(passwd:String)

    }
}