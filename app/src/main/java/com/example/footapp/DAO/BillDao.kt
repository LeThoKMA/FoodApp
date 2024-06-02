package com.example.footapp.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.footapp.model.Bill
import com.example.footapp.model.BillWithItems
import com.example.footapp.model.ItemBill

@Dao
interface BillDao {
    @Insert
    suspend fun insertBill(bill: Bill): Long

    @Insert
    suspend fun insertItem(item: ItemBill)

    @Transaction
    @Query("SELECT * FROM bills WHERE billId = :billId")
    suspend fun getBillWithItems(billId: Int): BillWithItems

    @Transaction
    @Query("SELECT * FROM bills")
    suspend fun getAllBillItem(): List<BillWithItems>
}
