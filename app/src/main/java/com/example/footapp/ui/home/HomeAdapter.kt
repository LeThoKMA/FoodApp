package com.example.footapp.ui.home

import android.content.Intent
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.footapp.R
import com.example.footapp.databinding.ItemTableBinding
import com.example.footapp.databinding.ItemUserBinding
import com.example.footapp.model.Table
import com.example.footapp.model.User
import com.example.footapp.ui.Oder.CartActivity
import com.example.footapp.utils.TABLE_POSITION

class HomeAdapter(var list: ArrayList<Table>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
        (holder as ViewHolder).bind(position,list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(var binding: ItemTableBinding) : RecyclerView.ViewHolder(binding.root) {
        var mLastClickTime:Long=0
        fun bind(position: Int,item:Table) {

            if (item.status == true) {
                binding.check.visibility = View.VISIBLE
            } else {
                binding.check.visibility = View.INVISIBLE

            }
            binding.tbPos.text = position.plus(1).toString()
            binding.parent.setOnClickListener {
                var intent = Intent(binding.root.context, CartActivity::class.java)
                intent.putExtra(TABLE_POSITION, position)
                binding.root.context.startActivity(intent)
            }
            if(item.status==true) {
                binding.check.setOnClickListener {

                    if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                        binding.check.visibility = View.INVISIBLE
                    }
                    mLastClickTime=SystemClock.elapsedRealtime()
                }
            }
        }

    }
    fun updateView(position: Int)
    {
        list[position].status=true
        notifyItemChanged(position)
    }

}