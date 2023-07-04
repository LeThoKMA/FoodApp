package com.example.footapp.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.footapp.OrderStatus
import com.example.footapp.R
import com.example.footapp.databinding.ItemOrderBinding
import com.example.footapp.model.OrderItem
import com.example.footapp.utils.formatToPrice
import com.example.footapp.utils.padZero

class OrderListAdapter(var list: MutableList<OrderItem>, val onClick: (Int) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.item_order,
            parent,
            false,
        ) as ItemOrderBinding
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(list[position], onClick)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(var binding: ItemOrderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: OrderItem, onClick: (Int) -> Unit) {
            binding.orderId.text = item.id.padZero()
            binding.dateTime.text = item.time
            binding.orderPrice.text = item.totalPrice.formatToPrice()
            binding.orderStatus.text = when (item.status) {
                OrderStatus.COMPLETED.ordinal -> OrderStatus.COMPLETED.name
                OrderStatus.CANCELLED.ordinal -> OrderStatus.CANCELLED.name
                OrderStatus.PREPAID.ordinal -> OrderStatus.PREPAID.name
                else -> OrderStatus.PENDING.name
            }
            binding.orderStatus.setTextColor(
                when (item.status) {
                    OrderStatus.COMPLETED.ordinal -> ContextCompat.getColor(
                        binding.root.context,
                        R.color.blue174BDD,
                    )
                    OrderStatus.CANCELLED.ordinal -> ContextCompat.getColor(
                        binding.root.context,
                        R.color.statusPendingInOrderText,
                    )
                    else -> ContextCompat.getColor(
                        binding.root.context,
                        R.color.statusActiveText,
                    )
                },
            )
            binding.orderStatus.setBackgroundColor(
                when (item.status) {
                    OrderStatus.COMPLETED.ordinal -> ContextCompat.getColor(
                        binding.root.context,
                        R.color.blue,
                    )
                    OrderStatus.CANCELLED.ordinal -> ContextCompat.getColor(
                        binding.root.context,
                        R.color.statusPending,
                    )
                    else -> ContextCompat.getColor(
                        binding.root.context,
                        R.color.statusActive,
                    )
                },
            )
            binding.root.setOnClickListener {
                item.id?.let { it1 -> onClick.invoke(it1) }
            }
        }
    }
}
