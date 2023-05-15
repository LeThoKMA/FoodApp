package com.example.footapp.presenter

import android.content.Context
import com.example.footapp.BaseViewModel
import com.example.footapp.DAO.DAO
import com.example.footapp.MyPreference
import com.example.footapp.model.Bill
import com.example.footapp.model.DetailItemChoose
import com.example.footapp.model.Item
import com.example.footapp.model.ItemBill
import com.example.footapp.utils.SIMPLE_DATE_FORMAT
import java.text.SimpleDateFormat
import java.util.*

class PayConfirmViewModel(
    var context: Context,
) : BaseViewModel() {
    var simpleDateFormat = SimpleDateFormat(SIMPLE_DATE_FORMAT)
    private var myPreference = MyPreference().getInstance(context)
    private var items: List<Item?> = arrayListOf()
    private var dao = DAO()
    var size = 0

    fun payConfirm(map: HashMap<Int, DetailItemChoose>, passwd: String, totalPrice: Int) {
        if (!checkPassword(passwd)) {
            return
        }
        val itemBills: ArrayList<ItemBill> = arrayListOf()

        for (item in map) {
            itemBills.add(
                ItemBill(
                    item.key,
                    item.value.name.toString(),
                    item.value.count,
                    item.value.price,
                ),
            )
        }

        for (item in items) {
            for (itemBill in itemBills) {
                if (item?.id == itemBill.id) {
                    val amount = itemBill.count?.let { item?.amount?.minus(it) }
                    val map: HashMap<String, Any> = hashMapOf()
                    if (amount != null) {
                        map.put("amount", amount)
                    }
                    item?.let { dao.updateItem(it, map) }
                }
            }
        }

        val mapItemBill: HashMap<String, ArrayList<ItemBill>>? = hashMapOf()
        mapItemBill?.put("items", itemBills)
        val bill = Bill(
            size,
            myPreference?.getUser()?.id,
            myPreference?.getUser()?.name,
            mapItemBill,
            totalPrice,

            simpleDateFormat.format(Calendar.getInstance().time),
        )
        dao.addBill(bill)

        // callback.complete("Thành công", true)
    }

    fun checkPassword(passwd: String): Boolean {
        val user = myPreference?.getUser()
        if (passwd == user?.password) {
            return true
        } else {
            // callback.complete("Mật khẩu không chính xách", false)
        }
        return false
    }


}
