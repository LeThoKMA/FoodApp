package com.example.footapp.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.footapp.model.dbmodel.TypeDB

@Dao
interface TypeDBDao {
    @Query("SELECT COUNT(*) FROM TypeDB WHERE name = :name")
    suspend fun countByName(name: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(typeDB: TypeDB): Long

    @Transaction
    suspend fun insertIfNotExists(typeDB: TypeDB): Long {
        val count = countByName(typeDB.name)
        return if (count == 0) {
            insert(typeDB)
        } else {
            -1L // or any other value indicating the insert didn't happen
        }
    }

    @Transaction
    @Query("SELECT * FROM TypeDB")
    suspend fun getAllType(): List<TypeDB>
}