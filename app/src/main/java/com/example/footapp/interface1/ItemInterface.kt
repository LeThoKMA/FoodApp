package com.example.footapp.interface1

import com.example.footapp.model.Item

interface ItemInterface {
    fun deleteItem(pos: Int, item: Item?)
    fun updateItem(item: Item?)
    fun addItem(item: Item?)
}