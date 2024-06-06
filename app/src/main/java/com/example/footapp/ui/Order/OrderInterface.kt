package com.example.footapp.ui.Order

import com.example.footapp.model.DetailItemChoose
import com.example.footapp.model.Item

interface OrderInterface {
    fun addItemToBill(item: Item)

    fun removeItem(item: Item)
}
