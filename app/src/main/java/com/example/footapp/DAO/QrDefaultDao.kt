package com.example.footapp.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.footapp.model.dbmodel.ItemDB
import com.example.footapp.model.dbmodel.QrDefault

@Dao
interface QrDefaultDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertItem(qrDefault: QrDefault): Long

    @Query("SELECT COUNT(*) FROM QrDefault WHERE url = :url")
    suspend fun countByName(url: String): Int

    @Transaction
    suspend fun insertIfNotExists(qrDefault: QrDefault): Long {
        val count = countByName(qrDefault.url.toString())
        return if (count == 0) {
            insertItem(qrDefault)
        } else {
            -1L // or any other value indicating the insert didn't happen
        }
    }

    @Transaction
    @Query("SELECT * FROM QrDefault")
    suspend fun getQrDefault(): List<QrDefault>

}