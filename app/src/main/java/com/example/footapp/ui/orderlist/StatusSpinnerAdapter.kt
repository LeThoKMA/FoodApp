package com.example.footapp.ui.orderlist

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.footapp.OrderStatus
import com.example.footapp.R

class StatusSpinnerAdapter(var list: List<Int>, var context: Context) : BaseAdapter() {
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(p0: Int): Any {
        return list[p0] ?: ""
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.item_spinner, p2, false)
        val title: TextView = view.findViewById(R.id.item)
        title.text = when (list[p0]) {
            OrderStatus.ALL.ordinal -> OrderStatus.ALL.name
            OrderStatus.COMPLETED.ordinal -> OrderStatus.COMPLETED.name
            OrderStatus.CANCELLED.ordinal -> OrderStatus.CANCELLED.name
            OrderStatus.PREPAID.ordinal -> OrderStatus.PREPAID.name
            else -> OrderStatus.PENDING.name
        }
        return view
    }
}
