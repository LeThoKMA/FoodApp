package com.example.footapp.ui.orderlist

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
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
import com.example.footapp.ui.history.OrderListAdapter
import com.example.footapp.utils.toast

/**
 * A simple [Fragment] subclass.
 * Use the [OrderListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OrderListFragment : BaseFragment<FragmentOrderListBinding, OrderListViewModel>() {
    lateinit var orderListAdapter: OrderListAdapter
    val list = mutableListOf<OrderItem>()
    var page = 1
    var time: String? = null
    var status: Int? = null
    var orderId: Int? = null
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
        orderListAdapter = OrderListAdapter(list, onClick = { viewModel.getOrderDetail(it) })
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
                if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == list.size - 1) {
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
    }

    override fun observerLiveData() {
        viewModel.orderList.observe(this) {
            list.addAll(it)
            orderListAdapter.notifyDataSetChanged()
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
            orderListAdapter.notifyDataSetChanged()
        }

        viewModel.messageCancel.observe(this) {
            binding.root.context.toast(it)
            list.forEach { if (it.id == orderId) it.status = OrderStatus.CANCELLED.ordinal }
            orderListAdapter.notifyDataSetChanged()
        }
    }
}
