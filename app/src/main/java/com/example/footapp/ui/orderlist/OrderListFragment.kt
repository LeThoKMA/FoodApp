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
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.footapp.MainViewModel
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
class OrderListFragment() :
    BaseFragment<FragmentOrderListBinding, OrderListViewModel>() {
    var orderListAdapter: OrderListAdapter? = null
    val list = mutableListOf<OrderItem>()
    var page = 0
    var time: String? = null
    var status: Int? = null
    var orderId: Int? = null
    var snackBar: Snackbar? = null
    var timerSpinnerAdapter: TimerSpinnerAdapter? = null
    var statusSpinnerAdapter: StatusSpinnerAdapter? = null

    private val mainViewModel: MainViewModel by activityViewModels()
    override fun getContentLayout(): Int {
        return R.layout.fragment_order_list
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(),
        )[OrderListViewModel::class.java]
    }

    override fun initView() {
        val binding = binding!!
        binding.let { paddingStatusBar(it.root) }
        orderListAdapter = OrderListAdapter(object : OrderDetailCallBack {
            override fun callBack(id: Int) {
                viewModel.getOrderDetail(id)
            }
        })
        binding.rcOrders.layoutManager = LinearLayoutManager(requireContext())
        binding.rcOrders.adapter = orderListAdapter

        timerSpinnerAdapter = TimerSpinnerAdapter(listOf("Mới", "Cũ"), requireContext())
        binding.spinnerTime.adapter = timerSpinnerAdapter

        statusSpinnerAdapter = StatusSpinnerAdapter(listOf(0, 1, 2, 3, 4), requireContext())
        binding.spinnerStatus.adapter = statusSpinnerAdapter
    }

    override fun initListener() {
        val binding = binding!!
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
                page = 0
                list.clear()
                if (p2 == 0) {
                    time = "latest"
                } else {
                    time = "oldest"
                }
                viewModel.resetTotalPage()
                viewModel.fetchOrderList(page, time, status)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        binding.spinnerStatus.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                page = 0
                list.clear()
                if (p2 == OrderStatus.ALL.ordinal) {
                    status = null
                    viewModel.resetTotalPage()
                    viewModel.fetchOrderList(page, time, status)
                } else {
                    status = p2
                    viewModel.resetTotalPage()
                    viewModel.fetchOrderListWithStatus(page, time, status)
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        binding.refreshLayout.setOnRefreshListener {
            page = 0
            list.clear()
            viewModel.resetTotalPage()
            viewModel.fetchOrderList(0, time, status)
            snackBar?.dismiss()
            mainViewModel.onRefreshDataInOrderList()
        }
    }

    override fun observerLiveData() {
        val binding = binding!!
        viewModel.orderList.observe(viewLifecycleOwner) {
            binding.refreshLayout.isRefreshing = false
            list.addAll(it)
            orderListAdapter?.submitList(list)
            orderListAdapter?.notifyDataSetChanged()
        }

        viewModel.orderListStatus.observe(viewLifecycleOwner) {
            binding.refreshLayout.isRefreshing = false
            list.addAll(it)
            orderListAdapter?.submitList(list)
            orderListAdapter?.notifyDataSetChanged()
        }
        viewModel.orderDetail.observe(viewLifecycleOwner) {
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
        viewModel.messageConfirm.observe(viewLifecycleOwner) {
            requireContext().toast(it)
            list.forEach { if (it.id == orderId) it.status = OrderStatus.COMPLETED.ordinal }
            val orders = list.toList()
            orderListAdapter?.submitList(orders)
            orderListAdapter?.notifyDataSetChanged()
        }

        viewModel.messageCancel.observe(viewLifecycleOwner) {
            requireContext().toast(it)
            list.forEach { if (it.id == orderId) it.status = OrderStatus.CANCELLED.ordinal }
            val orders = list.toList()
            orderListAdapter?.submitList(orders)
            orderListAdapter?.notifyDataSetChanged()
        }
    }

    @SuppressLint("ResourceAsColor")
    fun showSnackBar() {
        snackBar =
            this.view?.let {
                Snackbar.make(
                    it,
                    "Trượt xuống để xem đơn hàng mới nhất !!!",
                    Snackbar.LENGTH_INDEFINITE,
                )
            }
        val snackbarText =
            snackBar?.view?.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)

        val textSizeInSp = 18f // Kích thước chữ mong muốn (theo sp)

        val layoutParams = snackBar!!.view.layoutParams as FrameLayout.LayoutParams
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL
        layoutParams.topMargin = 75
        snackBar!!.view.layoutParams = layoutParams
        snackBar!!.view.setBackgroundColor(R.color.white)
        snackbarText?.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSizeInSp)
        snackBar?.show()
    }

    override fun onPause() {
        super.onPause()
        binding?.refreshLayout?.isEnabled = false
    }

    override fun onResume() {
        super.onResume()
        binding?.refreshLayout?.isEnabled = true
    }

    override fun onStop() {
        binding?.refreshLayout?.setOnRefreshListener(null)
        binding?.refreshLayout?.removeAllViews()
        super.onStop()
    }

    override fun onDestroyView() {
        binding?.refreshLayout?.removeAllViews()
        binding?.refreshLayout?.setOnRefreshListener(null)
        orderListAdapter = null
        statusSpinnerAdapter = null
        timerSpinnerAdapter = null
        viewModel.orderList.removeObservers(viewLifecycleOwner)
        viewModel.orderListStatus.removeObservers(viewLifecycleOwner)
        viewModel.orderDetail.removeObservers(viewLifecycleOwner)
        viewModel.messageCancel.removeObservers(viewLifecycleOwner)
        viewModel.messageConfirm.removeObservers(viewLifecycleOwner)
        super.onDestroyView()
    }
}
