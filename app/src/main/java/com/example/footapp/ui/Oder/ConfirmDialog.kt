package com.example.footapp.ui.Oder

import android.os.Bundle
import android.view.View
import com.example.footapp.ui.BaseDialog
import com.example.footapp.R
import com.example.footapp.databinding.CountDialogBinding
import com.example.footapp.databinding.FilterDialogBinding

open class ConfirmDialog(var callback: CallBack) :
    BaseDialog<CountDialogBinding>() {
    override fun getLayoutResource(): Int {
        return R.layout.count_dialog
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
        fun accept(count:String)

    }
}