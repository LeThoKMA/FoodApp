package com.example.footapp.ui.orderlist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.footapp.ItemSize
import com.example.footapp.R
import com.example.footapp.databinding.ItemCatgoryBinding
import com.example.footapp.databinding.ItemDetailCatgoryBinding
import com.example.footapp.model.BillItem
import com.example.footapp.utils.formatToPrice

class ItemAdapter(val context: Context, var list: MutableList<BillItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.item_detail_catgory,
            parent,
            false,
        ) as ItemDetailCatgoryBinding
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(val binding: ItemDetailCatgoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BillItem) {
            if (item.item?.imgUrl?.isNotEmpty() == true) {
                Glide.with(binding.root.context)
                    .load(item.item.imgUrl!![0]).into(binding.ivProduct)
            } else {
                binding.ivProduct.setImageResource(R.drawable.ic_picture_nodata)
            }
            binding.tvNameProduct.text = item.item?.name
            binding.amount.text = item.quantity.toString()
            binding.tvPrice.text = item.price.formatToPrice()
            binding.amount.text = "x: ${item.quantity}"
            binding.tvSize.text = when (item.size) {
                ItemSize.S.ordinal -> ItemSize.S.name
                ItemSize.M.ordinal -> ItemSize.M.name
                ItemSize.L.ordinal -> ItemSize.L.name
                else -> ""
            }
            binding.tvDescription.text = item.description?:""
            println(item.description)
        }
    }
}
