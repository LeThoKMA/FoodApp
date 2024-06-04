package com.example.footapp.model.dbmodel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TypeDB")
data class TypeDB(
    @PrimaryKey
    val id: Int = 0,
    val name: String = ""
)