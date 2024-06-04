package com.example.footapp.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.footapp.model.Bill
import com.example.footapp.model.BillWithItems
import com.example.footapp.model.ItemBill
import com.example.footapp.model.dbmodel.ItemDB

@Dao
interface BillDao {
    @Insert
    suspend fun insertBill(bill: Bill): Long

    @Insert
    suspend fun insertItem(item: ItemBill)

    @Delete
    suspend fun deleteBill(bill: Bill)

    @Transaction
    @Query("SELECT * FROM bills WHERE billId = :billId")
    suspend fun getBillWithItems(billId: Int): BillWithItems

    @Transaction
    @Query("SELECT * FROM bills")
    suspend fun getAllBillItem(): List<BillWithItems>
}
