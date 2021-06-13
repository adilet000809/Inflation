package com.example.diploma.ui.purchaseList

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PurchaseListProduct(
    @PrimaryKey val id: Int,
    @ColumnInfo(name="price") val price: Double,
    @ColumnInfo(name="name") val name: String,
    @ColumnInfo(name="quantity") var quantity: Int
) {
    fun getTotal(): Double {
        return price * quantity
    }
}
