package com.example.footapp.ui.Order.offline

import com.example.footapp.DAO.BillDao
import com.example.footapp.model.Bill
import com.example.footapp.model.ItemBill
import com.example.footapp.model.ItemBillRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OfflineRepository @Inject constructor(
    private val billDao: BillDao,
) {
    suspend fun insertBillWithItems(bill: Bill, items: List<ItemBill>) =
        withContext(Dispatchers.IO) {
            flow {
                emit(addBill(bill, items))
            }
        }

    private suspend fun addBill(bill: Bill, items: List<ItemBill>) {
        val billId = billDao.insertBill(bill)
        items.forEach { item ->
            item.billOwnerId = billId
            billDao.insertItem(item)
        }
    }
}
