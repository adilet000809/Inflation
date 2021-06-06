package com.example.diploma.ui.history

import com.example.diploma.ui.productInfo.Product
import java.util.*

data class PurchaseHistory(
    val id: Int?,
    val date: Date?,
    val total: Double,
    val purchaseProducts: List<HistoryProduct>
)

data class HistoryProduct(
    val id: Int?,
    val quantity: Int?,
    val price: Double?,
    val product: Product?
)
