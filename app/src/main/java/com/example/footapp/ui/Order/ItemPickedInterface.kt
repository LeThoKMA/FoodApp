package com.example.footapp.ui.Order

import com.example.footapp.model.DetailItemChoose

interface ItemPickedInterface {
    fun plus(detailItemChoose: DetailItemChoose)
    fun minus(detailItemChoose: DetailItemChoose)
}