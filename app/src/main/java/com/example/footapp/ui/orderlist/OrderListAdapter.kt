package com.example.footapp.ui.history

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.footapp.OrderStatus
import com.example.footapp.R
import com.example.footapp.databinding.ItemOrderBinding
import com.example.footapp.model.OrderItem

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
            binding.orderId.text = item.id.toString()
            binding.dateTime.text = item.time
            binding.orderPrice.text = item.totalPrice.toString()
            binding.orderStatus.text = when (item.status) {
                OrderStatus.COMPLETED.ordinal -> OrderStatus.COMPLETED.name
                OrderStatus.CANCELLED.ordinal -> OrderStatus.CANCELLED.name
                OrderStatus.PREPAID.ordinal -> OrderStatus.PREPAID.name
                else -> OrderStatus.PENDING.name
            }
            binding.orderStatus.setTextColor(
                when (item.status) {
                    OrderStatus.COMPLETED.ordinal -> Color.GREEN
                    OrderStatus.CANCELLED.ordinal -> Color.RED
                    else -> Color.YELLOW
                },
            )
            binding.root.setOnClickListener {
                item.id?.let { it1 -> onClick.invoke(it1) }
            }
        }
    }
}
