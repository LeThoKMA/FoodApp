package com.example.footapp.ui.Oder

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.footapp.R
import com.example.footapp.databinding.ItemCatgoryBinding
import com.example.footapp.model.DetailItemChoose
import com.example.footapp.model.Item
import com.example.footapp.model.ItemInRecycler
import com.example.footapp.presenter.OderPresenter

class OderAdapter(var list: ArrayList<Item?>, var oderPresenter: OderPresenter) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var listState: HashMap<String, Boolean> = hashMapOf()
    var listCount: HashMap<Int, ItemInRecycler> = hashMapOf()


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

        list[position]?.let {
            (holder as ViewHolder).bind(
                position,
                it,
                oderPresenter,
                listState,
                listCount
            )
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(var binding: ItemCatgoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            position: Int,
            item: Item?,
            callback: OderPresenter,
            listState: HashMap<String, Boolean>,
            listCount: HashMap<Int, ItemInRecycler>
        ) {
            Glide.with(binding.root.context).load(item?.imgUrl).into(binding.ivProduct)
            binding.tvNameProduct.text = item?.name
            binding.amount.text = item?.amount.toString()
            binding.tvPrice.text = item?.price.toString()


            if (listCount.containsKey(item?.id)) {
                listCount[item?.id]?.let { binding.edtNumber.setText(it.count.toString()) }
            } else {
                binding.edtNumber.setText("0")
            }
            if (listState.containsKey(item?.id.toString())) {
                if(listState[item?.id.toString()] == true)
                {
                    binding.ivCheck.isChecked =true
                        binding.edtNumber.isEnabled=true
                }
                else
                {
                    binding.edtNumber.isEnabled=false
                    binding.ivCheck.isChecked = false
                }
            } else {
                binding.ivCheck.isChecked = false
                binding.edtNumber.isEnabled=false

            }
            binding.ivCheck.setOnClickListener {
                if (binding.ivCheck.isChecked) {
                    listState.set(item?.id.toString(), true)
                    binding.edtNumber.isEnabled=true

                } else {
                    listState.set(item?.id.toString(), false)
                    binding.edtNumber.isEnabled=false
                }
                item?.let { it1 -> check(position, callback, it1, listState, listCount) }


            }

            binding.ivUp.setOnClickListener {
                var count = binding.edtNumber.text.toString().toInt()
                if (binding.edtNumber.text.toString().toInt() < item?.amount!!
                ) {
                    binding.edtNumber.setText(count.plus(1).toString())
                } else {
                    binding.edtNumber.setText(item?.amount!!.toString())
                }
                // listCount.set(position, binding.edtNumber.text.toString().toInt())
                item?.let { it1 -> check(position, callback, it1, listState, listCount) }

            }
            binding.edtNumber.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    if(listState.containsKey(item?.id.toString()))
                    {
                        if(listState[item?.id.toString()]==true)
                        {
                            if (listCount.containsKey(item?.id)) {
                                Log.e("TAG", listCount.toString())
                                    if (binding.edtNumber.text.toString()
                                            .toInt() > listCount[item?.id]?.amount!!
                                    ) {
                                        binding.edtNumber.setText(listCount[item?.id]?.amount.toString())
                                    }

                                }
                            else
                            {
                                if (binding.edtNumber.text.toString()
                                        .toInt() > item?.amount!!
                                ) {
                                    binding.edtNumber.setText(item.amount.toString())
                                }

                                Log.e("1111111", item.toString())

                            }
                            item?.let { it1 -> check(position, callback, it1, listState, listCount) }
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
                //   listCount.set(position, binding.edtNumber.text.toString().toInt())

                item?.let { it1 -> check(position, callback, it1, listState, listCount) }


            }

        }

        fun check(
            position: Int,
            callback: OderPresenter,
            item: Item,
            listState: HashMap<String, Boolean>,
            listCount: HashMap<Int, ItemInRecycler>
        ) {
            if (listState[item.id.toString()] == true) {

         if(binding.edtNumber.text.toString().isNotBlank()&&binding.edtNumber.text.toString().toInt()>0) {
             var priceItem = binding.edtNumber.text.toString().toInt() * (item?.price ?: 0)

             listCount.set(
                 item?.id!!,
                 ItemInRecycler(
                     item.id,
                     priceItem,
                     binding.edtNumber.text.toString().toInt(),
                     item.amount
                 )
             )
             var detailBill = DetailItemChoose(
                 item?.id,
                 item?.name,
                 listCount[item?.id]?.count,
                 listCount[item?.id]?.price,
                 item?.price,
                 item?.imgUrl

             )
             callback.addItemToBill(detailBill)

         }
            } else {
                var detailBill = DetailItemChoose(item?.id, item?.name, 0, 0)

                callback.addItemToBill(detailBill)

            }
        }

    }

}