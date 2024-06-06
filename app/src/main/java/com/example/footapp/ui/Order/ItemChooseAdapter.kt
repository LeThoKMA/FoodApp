package com.example.footapp.ui.Order

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.footapp.R
import com.example.footapp.databinding.ItemCatgoryBinding
import com.example.footapp.databinding.ItemPickedBinding
import com.example.footapp.model.DetailItemChoose
import com.example.footapp.utils.formatToPrice

class ItemChooseAdapter(
    private val list: List<DetailItemChoose>,
    private val itemPickedInterface: ItemPickedInterface
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.item_picked,
            parent,
            false,
        ) as ItemPickedBinding
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(list[position], itemPickedInterface)
    }


    class ViewHolder(val binding: ItemPickedBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DetailItemChoose, itemPickedInterface: ItemPickedInterface) {
            if (item.imgUrl?.isNotEmpty() == true) {
                Glide.with(binding.root.context)
                    .load(item.imgUrl!![0]).into(binding.ivProduct)
            } else {
                binding.ivProduct.setImageResource(R.drawable.ic_picture_nodata)
            }
            binding.tvNameProduct.text = item.name
            binding.edtNumber.text = item.count.toString()
            binding.tvPrice.text = item.price.formatToPrice()
            binding.ivUp.setOnClickListener { itemPickedInterface.plus(item) }
            binding.ivDown.setOnClickListener { itemPickedInterface.minus(item) }
        }
    }
}
