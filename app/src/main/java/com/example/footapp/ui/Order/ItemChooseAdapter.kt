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
import com.example.footapp.model.DetailItemChoose
import com.example.footapp.utils.formatToPrice

class ItemChooseAdapter() :
    ListAdapter<DetailItemChoose, RecyclerView.ViewHolder>(object :
        DiffUtil.ItemCallback<DetailItemChoose>() {
        override fun areItemsTheSame(
            oldItem: DetailItemChoose,
            newItem: DetailItemChoose,
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: DetailItemChoose,
            newItem: DetailItemChoose,
        ): Boolean {
            return oldItem == newItem
        }
    }) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.item_catgory,
            parent,
            false,
        ) as ItemCatgoryBinding
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(getItem(position))
    }

    class ViewHolder(val binding: ItemCatgoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DetailItemChoose) {
            if (item.imgUrl?.isNotEmpty() == true) {
                Glide.with(binding.root.context)
                    .load(item.imgUrl!![0]).into(binding.ivProduct)
            } else {
                binding.ivProduct.setImageResource(R.drawable.ic_picture_nodata)
            }
            binding.tvNameProduct.text = item.name
            binding.amount.text = item.count.toString()
            binding.tvPrice.text = item.price.formatToPrice()
            binding.ivUp.visibility = View.INVISIBLE
            binding.ivDown.visibility = View.INVISIBLE
            binding.ivCheck.visibility = View.INVISIBLE
        }
    }
}
