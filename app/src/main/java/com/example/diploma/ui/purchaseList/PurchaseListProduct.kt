package com.example.diploma.ui.purchaseList

import com.example.diploma.ui.productInfo.SupermarketProduct

data class PurchaseListProduct(
    val supermarketProduct: SupermarketProduct,
    var quantity: Int
) {
    fun getTotal(): Double {
        return supermarketProduct.price?.times(quantity) ?: 0.0
    }
}
