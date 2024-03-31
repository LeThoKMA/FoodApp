package com.example.footapp.ui.customer

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import com.example.footapp.R
import com.example.footapp.base.BaseDialog
import com.example.footapp.databinding.QrDialogBinding

class QrDialog : BaseDialog<QrDialogBinding>() {
    override fun getLayoutResource(): Int {
        return R.layout.qr_dialog
    }

    override fun init(saveInstanceState: Bundle?, view: View?) {
    }

    override fun setUp(view: View?) {
        val qrData = arguments?.getByteArray("qr")
        qrData?.let {
            binding.imgQr.setImageBitmap(
                BitmapFactory.decodeByteArray(
                    qrData,
                    0,
                    qrData.size,
                ),
            )
        }
    }

    override fun onStart() {
        super.onStart()
        if (dialog?.window != null) {
            val width = resources.displayMetrics.widthPixels // Chiều rộng của màn hình
            val height = resources.displayMetrics.heightPixels // Chiều cao của màn hình
            val desiredWidth =
                (width * 0.5).toInt() // Kích thước mong muốn, ở đây là 80% chiều rộng màn hình
            val desiredHeight =
                (height * 0.5).toInt() // Kích thước mong muốn, ở đây là 60% chiều cao màn hình
            activity?.window?.decorView?.width?.let {
                dialog?.window?.setLayout(
                    desiredWidth,
                    desiredHeight,
                )
            }
        }
    }
}
