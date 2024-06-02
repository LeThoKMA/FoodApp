package com.example.footapp.DAO

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.footapp.model.Bill
import com.example.footapp.model.ItemBill

@Database(entities = [ItemBill::class, Bill::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun billDao(): BillDao
}
