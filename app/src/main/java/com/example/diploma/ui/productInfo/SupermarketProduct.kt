package com.example.diploma.ui.productInfo

import com.example.diploma.ui.home.Supermarket

data class SupermarketProduct(
    val id: Int?,
    val price: Double?,
    val discount: Double?,
    val product: Product,
    val supermarket: Supermarket
)