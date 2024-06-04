package com.example.footapp.model.dbmodel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "BannerDB")
data class BannerDB(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val url: String?
)