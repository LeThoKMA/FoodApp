package com.example.footapp.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.footapp.R
import com.example.footapp.databinding.ItemBillBinding
import com.example.footapp.model.Bill

class HistoryAdapter(var list:ArrayList<Bill>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.item_bill,
            parent,
            false
        ) as ItemBillBinding
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
    class ViewHolder(var binding:ItemBillBinding):RecyclerView.ViewHolder(binding.root)
    {
        fun bind(item:Bill)
        {
            binding.tvName.text=item.username
            binding.tvTime.text=item.dateTime
            var detail=""
           item.items?.values?.forEach { it.forEach { detail+=it.name+"-"+it.count+"-"+it.price +"\n" } }
            binding.tvDetail.text=detail
            binding.tvTotal.text=item.totalPrice.toString()
            binding.root.setOnClickListener {  }
        }
    }
}