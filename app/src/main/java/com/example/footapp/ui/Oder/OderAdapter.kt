package com.example.footapp.ui.Oder

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.footapp.R
import com.example.footapp.databinding.ItemCatgoryBinding
import com.example.footapp.model.DetailItemChoose
import com.example.footapp.model.Item
import com.example.footapp.presenter.OderPresenter

class OderAdapter(var list: ArrayList<Item?>, var oderPresenter: OderPresenter) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.item_catgory,
            parent,
            false
        ) as ItemCatgoryBinding
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        list[position]?.let { (holder as ViewHolder).bind(position, it, oderPresenter) }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(var binding: ItemCatgoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int, item: Item?, callback: OderPresenter) {
            Glide.with(binding.root.context).load(item?.imgUrl).into(binding.ivProduct)
            binding.tvNameProduct.text = item?.name
            binding.amount.text = item?.amount.toString()
            binding.tvPrice.text = item?.price.toString()
            binding.edtNumber.setText("0")
            binding.ivCheck.setOnClickListener {
                if (binding.ivCheck.isChecked) {

                    var priceItem = binding.edtNumber.text.toString().toInt() * (item?.price ?: 0)
                    var detailBill = DetailItemChoose(
                        item?.id,
                        item?.name,
                        binding.edtNumber.text.toString().toInt(),
                        priceItem,
                        item?.price,
                        item?.imgUrl

                    )

                    callback.addItemToBill(detailBill)

                } else {
                    var detailBill = DetailItemChoose(item?.id, item?.name, 0, 0)
                    callback.addItemToBill(detailBill)

                }
            }
            binding.ivUp.setOnClickListener {
                var count = binding.edtNumber.text.toString().toInt()
                if (binding.edtNumber.text.toString().toInt() < binding.amount.text.toString()
                        .toInt()
                ) {
                    binding.edtNumber.setText(count.plus(1).toString())
                } else {
                    binding.edtNumber.setText(binding.amount.text.toString())
                }

                if (binding.ivCheck.isChecked) {
                    var priceItem = binding.edtNumber.text.toString().toInt() * (item?.price ?: 0)
                    var detailBill = DetailItemChoose(
                        item?.id,
                        item?.name,
                        binding.edtNumber.text.toString().toInt(),
                        priceItem,
                        item?.price,
                        item?.imgUrl
                    )
                    callback.addItemToBill(detailBill)

                } else {
                    var detailBill = DetailItemChoose(item?.id, item?.name, 0, 0)
                    callback.addItemToBill(detailBill)

                }
            }
            binding.edtNumber.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    if (binding.edtNumber.text.toString().isNotBlank()) {
                        if (binding.edtNumber.text.toString()
                                .toInt() > binding.amount.text.toString().toInt()
                        ) {
                            binding.edtNumber.setText(binding.amount.text.toString())
                        }


                        if (binding.ivCheck.isChecked) {
                            var priceItem =
                                binding.edtNumber.text.toString().toInt() * (item?.price ?: 0)
                            var detailBill = DetailItemChoose(
                                item?.id,
                                item?.name,
                                binding.edtNumber.text.toString().toInt(),
                                priceItem,
                                item?.price,
                                item?.imgUrl

                            )
                            callback.addItemToBill(detailBill)

                        } else {
                            var detailBill = DetailItemChoose(item?.id, item?.name, 0, 0)
                            callback.addItemToBill(detailBill)
                        }
                    }

                    //  }
                }

            })
            binding.ivDown.setOnClickListener {
                var count = binding.edtNumber.text.toString().toInt()
                if (binding.edtNumber.text.toString().toInt() > 0) {
                    binding.edtNumber.setText(count.minus(1).toString())
                } else {
                    binding.edtNumber.setText("0")
                }

                if (binding.ivCheck.isChecked) {

                    if (binding.ivCheck.isChecked) {
                        var priceItem =
                            binding.edtNumber.text.toString().toInt() * (item?.price ?: 0)
                        var detailBill = DetailItemChoose(
                            item?.id,
                            item?.name,
                            binding.edtNumber.text.toString().toInt(),
                            priceItem,
                            item?.price,
                            item?.imgUrl

                        )
                        callback.addItemToBill(detailBill)
                    } else {
                        var detailBill = DetailItemChoose(item?.id, item?.name, 0, 0)
                        callback.addItemToBill(detailBill)

                    }
                }

            }

        }


    }
}