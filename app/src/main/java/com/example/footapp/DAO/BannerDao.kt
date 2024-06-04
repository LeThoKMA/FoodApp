package com.example.footapp.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.footapp.model.dbmodel.BannerDB

@Dao
interface BannerDao {

    @Query("SELECT COUNT(*) FROM BannerDB WHERE url = :url")
    suspend fun countByName(url: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBanner(bannerDB: BannerDB): Long

    suspend fun insertIfNotExists(bannerDB: BannerDB): Long {
        val count = countByName(bannerDB.url.toString())
        return if (count == 0) {
            insertBanner(bannerDB = bannerDB)
        } else {
            -1L // or any other value indicating the insert didn't happen
        }
    }

    @Transaction
    @Query("SELECT * FROM BannerDB")
    suspend fun getAllBanner(): List<BannerDB>
}