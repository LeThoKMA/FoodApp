package com.example.footapp.ui.history

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.footapp.R
import com.example.footapp.databinding.ItemStaffBinding
import com.example.footapp.model.StaffData
import com.example.footapp.utils.formatToPrice
import com.example.footapp.utils.padZero

class StatisticStaffAdapter(var list: MutableList<StaffData>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.item_staff,
            parent,
            false,
        ) as ItemStaffBinding
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(var binding: ItemStaffBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: StaffData) {
            Log.e("datta", item.toString())
            if (item != StaffData()) {
                binding.staffId.text = item.id.padZero()
                binding.tvName.text = item.fullname
                binding.tvTime.text = item.duration.toString()
                binding.tvRevenue.text = item.revenue.formatToPrice()
            }
        }
    }

    fun setData(list: MutableList<StaffData>) {
        if (!this.list.equals(list)) {
            this.list = list
            notifyDataSetChanged()
        }
    }
}
