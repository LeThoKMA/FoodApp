package com.example.footapp.DAO

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.footapp.model.Bill
import com.example.footapp.model.ItemBill
import com.example.footapp.model.dbmodel.BannerDB
import com.example.footapp.model.dbmodel.ItemDB
import com.example.footapp.model.dbmodel.TypeDB

@Database(
    entities = [ItemBill::class, Bill::class, ItemDB::class, TypeDB::class, BannerDB::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun billDao(): BillDao

    abstract fun itemDao(): ItemDao

    abstract fun typeDao(): TypeDBDao

    abstract fun bannerDao(): BannerDao
}
