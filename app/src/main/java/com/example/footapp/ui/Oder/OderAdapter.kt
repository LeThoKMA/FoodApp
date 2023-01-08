package com.example.footapp.ui.Oder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
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
    var listState: HashMap<String, Boolean> = hashMapOf()
    var listCount:HashMap<Int,Int> = hashMapOf()


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
            listCount:HashMap<Int,Int>

        ) {
            Glide.with(binding.root.context).load(item?.imgUrl).into(binding.ivProduct)
            binding.tvNameProduct.text = item?.name
            binding.amount.text = item?.amount.toString()
            binding.tvPrice.text = item?.price.toString()

            if(listCount.containsKey(position))
            {
                binding.edtNumber.text =listCount[position].toString()
            }
            else
            {
                binding.edtNumber.text = "0"
            }
            if (listState.containsKey(item?.id.toString())) {
                binding.ivCheck.isChecked = listState[item?.id.toString()] == true
            } else {
                binding.ivCheck.isChecked = false

            }
            binding.ivCheck.setOnClickListener {
                if (binding.ivCheck.isChecked) {
                    listState.set(item?.id.toString(), true)


                } else {
                    listState.set(item?.id.toString(), false)

                }
                item?.let { it1 -> check(position, callback, it1, listState) }


            }

            binding.ivUp.setOnClickListener {
                var count = binding.edtNumber.text.toString().toInt()
                if (binding.edtNumber.text.toString().toInt() < item?.amount!!
                ) {
                    binding.edtNumber.setText(count.plus(1).toString())
                } else {
                    binding.edtNumber.setText(item?.amount!!.toString())
                }
                 listCount.set(position, binding.edtNumber.text.toString().toInt())
                item?.let { it1 -> check(position, callback, it1, listState) }


            }
        binding.edtNumber.setOnClickListener {
            var dialog=ConfirmDialog(object :ConfirmDialog.CallBack{
                override fun accept(count: String) {
                    if (count.toInt()> item?.amount!!)
                    {
                        binding.edtNumber.text= item.amount.toString()
                    }
                    else
                    {
                        binding.edtNumber.text=count
                    }
                    listCount.put(position,binding.edtNumber.text.toString().toInt())
                    item?.let { it1 -> check(position, callback, it1, listState) }

                }
            })
            dialog.show((binding.root.context as AppCompatActivity).supportFragmentManager,"")
        }

            binding.ivDown.setOnClickListener {
                var count = binding.edtNumber.text.toString().toInt()
                if (binding.edtNumber.text.toString().toInt() > 0) {
                    binding.edtNumber.text = count.minus(1).toString()
                } else {
                    binding.edtNumber.text = "0"
                }
                listCount.put(position,binding.edtNumber.text.toString().toInt())
                //   listCount.set(position, binding.edtNumber.text.toString().toInt())

                item?.let { it1 -> check(position, callback, it1, listState) }


            }

        }

        fun check(
            position: Int,
            callback: OderPresenter,
            item: Item,
            listState: HashMap<String, Boolean>,
        ) {
            if (listState[item.id.toString()] == true) {

             var priceItem = binding.edtNumber.text.toString().toInt() * (item.price ?: 0)


             var detailBill = DetailItemChoose(
                 item?.id,
                 item?.name,
               binding.edtNumber.text.toString().toInt(),
                 priceItem,
                 item.price,
                 item.imgUrl
             )
             callback.addItemToBill(detailBill)

         }
            else {
                var detailBill = DetailItemChoose(item?.id, item?.name, 0, 0)

                callback.addItemToBill(detailBill)

            }
        }

    }
    fun updateData(count:String,pos:Int)
    {

    }

}