package com.example.footapp.ui.Order

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.footapp.R
import com.example.footapp.Response.CategoryResponse

class CategoryAdapter(
    val list: List<CategoryResponse>,
    val onClickItem: (Int) -> Unit,
) : RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_type, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(list[position], onClickItem)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvName: TextView = view.findViewById(R.id.tv_type)
        fun bind(item: CategoryResponse, onClickItem: (Int) -> Unit) {
            if (item.isPicked) {
                tvName.setBackgroundResource(R.drawable.bg_blue)
            } else {
                tvName.setBackgroundResource(R.drawable.bg_gray)

            }
            tvName.text = item.name
            tvName.setOnClickListener { onClickItem.invoke(item.id) }
        }
    }
}
