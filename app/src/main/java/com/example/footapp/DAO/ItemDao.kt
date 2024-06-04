package com.example.footapp.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.footapp.model.dbmodel.ItemDB
import com.example.footapp.model.dbmodel.TypeDB

@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertItem(itemDb: ItemDB): Long

    @Query("SELECT COUNT(*) FROM ItemDB WHERE name = :name")
    suspend fun countByName(name: String): Int

    @Transaction
    suspend fun insertIfNotExists(itemDb: ItemDB): Long {
        val count = countByName(itemDb.name.toString())
        return if (count == 0) {
            insertItem(itemDb)
        } else {
            -1L // or any other value indicating the insert didn't happen
        }
    }

    @Transaction
    @Query("SELECT * FROM ItemDB WHERE typeId = :idType")
    suspend fun getItemByType(idType: Int): List<ItemDB>

    @Transaction
    @Query("SELECT * FROM ItemDB")
    suspend fun getAllItem(): List<ItemDB>
}