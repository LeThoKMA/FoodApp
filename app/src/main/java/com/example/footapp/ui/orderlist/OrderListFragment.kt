package com.example.footapp.ui.orderlist

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.footapp.OrderStatus
import com.example.footapp.R
import com.example.footapp.ViewModelFactory
import com.example.footapp.base.BaseFragment
import com.example.footapp.databinding.FragmentOrderListBinding
import com.example.footapp.model.OrderItem
import com.example.footapp.ui.history.OrderDetailCallBack
import com.example.footapp.ui.history.OrderListAdapter
import com.example.footapp.utils.toast
import com.google.android.material.snackbar.Snackbar

/**
 * A simple [Fragment] subclass.
 * Use the [OrderListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OrderListFragment(val onRefreshData: () -> Unit) :
    BaseFragment<FragmentOrderListBinding, OrderListViewModel>() {
    lateinit var orderListAdapter: OrderListAdapter
    val list = mutableListOf<OrderItem>()
    var page = 1
    var time: String? = null
    var status: Int? = null
    var orderId: Int? = null
    var snackBar: Snackbar? = null
    lateinit var timerSpinnerAdapter: TimerSpinnerAdapter
    lateinit var statusSpinnerAdapter: StatusSpinnerAdapter
    override fun getContentLayout(): Int {
        return R.layout.fragment_order_list
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(binding.root.context),
        )[OrderListViewModel::class.java]
    }

    override fun initView() {
        paddingStatusBar(binding.root)
        orderListAdapter = OrderListAdapter(object : OrderDetailCallBack {
            override fun callBack(id: Int) {
                viewModel.getOrderDetail(id)
            }
        })
        binding.rcOrders.layoutManager = LinearLayoutManager(binding.root.context)
        binding.rcOrders.adapter = orderListAdapter

        timerSpinnerAdapter = TimerSpinnerAdapter(listOf("Mới", "Cũ"), binding.root.context)
        binding.spinnerTime.adapter = timerSpinnerAdapter

        statusSpinnerAdapter = StatusSpinnerAdapter(listOf(0, 1, 2, 3, 4), binding.root.context)
        binding.spinnerStatus.adapter = statusSpinnerAdapter
    }

    override fun initListener() {
        binding.rcOrders.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager: LinearLayoutManager =
                    recyclerView.layoutManager as LinearLayoutManager
                if (dy > 0 && linearLayoutManager.findLastCompletelyVisibleItemPosition() == list.size - 1) {
                    page++
                    viewModel.fetchOrderList(page, time, status)
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
        binding.spinnerTime.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                page = 1
                if (p2 == 0) {
                    time = "latest"
                    viewModel.fetchOrderList(page, time, status)
                } else {
                    time = "oldest"
                    viewModel.fetchOrderList(page, time, status)
                }
                list.clear()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        binding.spinnerStatus.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                page = 1
                if (p2 == OrderStatus.ALL.ordinal) {
                    status = null
                    viewModel.fetchOrderList(page, time, status)
                } else {
                    status = p2
                    viewModel.fetchOrderList(page, time, status)
                }
                list.clear()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        binding.refreshLayout.setOnRefreshListener {
            page = 1
            list.clear()
            viewModel.fetchOrderList(1, time, status)
            snackBar?.dismiss()
            onRefreshData.invoke()
        }
    }

    override fun observerLiveData() {
        viewModel.orderList.observe(this) {
            binding.refreshLayout.isRefreshing = false
            list.addAll(it)
            val orders = list.toList()
            orderListAdapter.submitList(orders)
        }
        viewModel.orderDetail.observe(this) {
            val bundle = Bundle()
            bundle.putParcelable("order_detail", it)
            val detailOrderDialog = DetailOrderDialog(
                onConfirm = {
                    viewModel.confirmBill(
                        it.id!!,
                        OrderStatus.COMPLETED.ordinal,
                        it.totalPrice!!,
                    )
                    orderId = it.id
                },
                onCancel = {
                    viewModel.cancelBill(
                        it.id!!,
                        OrderStatus.CANCELLED.ordinal,
                        it.totalPrice!!,
                    )
                    orderId = it.id
                },
            )
            detailOrderDialog.arguments = bundle
            detailOrderDialog.show(
                (binding.root.context as AppCompatActivity).supportFragmentManager,
                "",
            )
        }
        viewModel.messageConfirm.observe(this) {
            binding.root.context.toast(it)
            list.forEach { if (it.id == orderId) it.status = OrderStatus.COMPLETED.ordinal }
            val orders = list.toList()
            orderListAdapter.submitList(orders)
            orderListAdapter.notifyDataSetChanged()
        }

        viewModel.messageCancel.observe(this) {
            binding.root.context.toast(it)
            list.forEach { if (it.id == orderId) it.status = OrderStatus.CANCELLED.ordinal }
            val orders = list.toList()
            orderListAdapter.submitList(orders)
            orderListAdapter.notifyDataSetChanged()
        }
    }

    @SuppressLint("ResourceAsColor")
    fun showSnackBar() {
        snackBar =
            Snackbar.make(
                binding.root,
                "Trượt xuống để xem đơn hàng mới nhất !!!",
                Snackbar.LENGTH_INDEFINITE,
            )
        val snackbarText =
            snackBar!!.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)

        val textSizeInSp = 18f // Kích thước chữ mong muốn (theo sp)

        val layoutParams = snackBar!!.view.layoutParams as FrameLayout.LayoutParams
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL
        layoutParams.topMargin = 75
        snackBar!!.view.layoutParams = layoutParams
        snackBar!!.view.setBackgroundColor(R.color.white)
        snackbarText.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSizeInSp)
        snackBar?.show()
    }
}
