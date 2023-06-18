package com.example.footapp.ui.orderlist

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.footapp.R

class TimerSpinnerAdapter(var list: List<String>, var context: Context) : BaseAdapter() {
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
        val itemSpinner: String = list[p0]
        val title: TextView = view.findViewById(R.id.item)

        title.text = itemSpinner

        return view
    }
}
