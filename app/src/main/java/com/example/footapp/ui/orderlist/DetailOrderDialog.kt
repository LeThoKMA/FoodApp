package com.example.footapp.ui.orderlist

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.footapp.OrderStatus
import com.example.footapp.R
import com.example.footapp.Response.BillDetailResponse
import com.example.footapp.base.BaseDialog
import com.example.footapp.databinding.DetailOrderDialogBinding

class DetailOrderDialog(
    val onConfirm: (BillDetailResponse) -> Unit,
    val onCancel: (BillDetailResponse) -> Unit,
) : BaseDialog<DetailOrderDialogBinding>() {
    lateinit var adapter: ItemAdapter
    override fun getLayoutResource(): Int {
        return R.layout.detail_order_dialog
    }

    override fun init(saveInstanceState: Bundle?, view: View?) {
        val orderDetail = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("order_detail", BillDetailResponse::class.java)
        } else {
            arguments?.getParcelable("order_detail")
        }
        binding.tvTime.text = orderDetail?.time ?: ""
        adapter = ItemAdapter(
            binding.root.context,
            orderDetail?.billItemList?.toMutableList() ?: mutableListOf(),
        )
        binding.rcItem.layoutManager = LinearLayoutManager(binding.root.context)
        binding.rcItem.adapter = adapter
        binding.tvPrice.text = orderDetail?.totalPrice.toString() + " đ"
        binding.tvUsername.text = orderDetail?.user?.fullname
        val priceDiscount = (orderDetail?.usedPromotion?.percentage?.div(100f))?.times(
            orderDetail.totalPrice
                ?: 0,
        )?.toInt()
        binding.tvPromotionDiscount.text = "-$priceDiscount đ"
        if (orderDetail?.status == OrderStatus.COMPLETED.ordinal || orderDetail?.status == OrderStatus.CANCELLED.ordinal) {
            binding.tvAccept.visibility = View.GONE
            binding.tvCancel.visibility = View.GONE
        }
        binding.tvAccept.setOnClickListener {
            orderDetail?.let { it1 -> onConfirm.invoke(it1) }
            dismiss()
        }
        binding.tvCancel.setOnClickListener {
            orderDetail?.let { it1 ->
                onCancel.invoke(it1)
                dismiss()
            }
        }
    }

    override fun setUp(view: View?) {
    }

    override fun onStart() {
        super.onStart()
        if (dialog?.window != null) {
            val width = resources.displayMetrics.widthPixels // Chiều rộng của màn hình
            val height = resources.displayMetrics.heightPixels // Chiều cao của màn hình
            val desiredWidth =
                (width * 0.8).toInt() // Kích thước mong muốn, ở đây là 80% chiều rộng màn hình
            val desiredHeight =
                (height * 0.6).toInt() // Kích thước mong muốn, ở đây là 60% chiều cao màn hình
            activity?.window?.decorView?.width?.let {
                dialog?.window?.setLayout(
                    desiredWidth,
                    desiredHeight,
                )
            }
        }
    }
}
