package com.example.footapp

enum class OrderStatus {
    ALL,
    PENDING,
    COMPLETED,
    CANCELLED,
    PREPAID,
}

enum class ItemSize(val value: Double) {
    S(0.75),
    M(1.0),
    L(1.5),
}
