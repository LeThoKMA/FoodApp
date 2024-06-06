package com.example.footapp.ui.Order.offline

import com.example.footapp.DAO.BillDao
import com.example.footapp.DAO.ItemDao
import com.example.footapp.DAO.TypeDBDao
import com.example.footapp.model.Bill
import com.example.footapp.model.ItemBill
import com.example.footapp.model.ItemBillRequest
import com.example.footapp.model.dbmodel.ItemDB
import com.example.footapp.model.dbmodel.TypeDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OfflineRepository @Inject constructor(
    private val billDao: BillDao,
    private val typeDBDao: TypeDBDao,
    private val itemDao: ItemDao,
) {
    private val dispatcher = Dispatchers.IO

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
    suspend fun getAllType(): Flow<List<TypeDB>> = withContext(dispatcher) {
        flow { emit(typeDBDao.getAllType()) }
    }

    suspend fun getAllItemByType(idType: Int): Flow<List<ItemDB>> = withContext(dispatcher) {
        flow { emit(itemDao.getItemByType(idType)) }
    }

    suspend fun getAllItem(): Flow<List<ItemDB>> = withContext(dispatcher) {
        flow { emit(itemDao.getAllItem()) }
    }
}
