package com.example.footapp.ui.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.footapp.R
import com.example.footapp.databinding.ItemTableBinding
import com.example.footapp.databinding.ItemUserBinding
import com.example.footapp.model.User
import com.example.footapp.ui.Oder.CartActivity

class HomeAdapter(var list:ArrayList<Int>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate(
            layoutInflater,
           R.layout.item_table,
            parent,
            false
        ) as ItemTableBinding
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(var binding:ItemTableBinding):RecyclerView.ViewHolder(binding.root)
    {
        fun bind(position: Int)
        {


            binding.tbPos.text=position.toString()
            binding.parent.setOnClickListener {
                var intent=Intent(binding.root.context,CartActivity::class.java)
               // intent.putExtra("user",user)
                binding.root.context.startActivity(intent)
            }
        }

    }


}