package com.example.diploma.data.api.product

import com.example.diploma.data.model.BaseResponse
import com.example.diploma.ui.productInfo.ProductList
import com.example.diploma.ui.productInfo.SupermarketProduct
import com.example.diploma.ui.purchaseList.PurchaseListProduct
import com.example.diploma.ui.scan.PurchaseProduct
import retrofit2.Response
import retrofit2.http.Header
import javax.inject.Inject

interface ProductApiHelper {
    suspend fun fetchProduct(barcode: String, supermarketId: Int): Response<SupermarketProduct>
    suspend fun fetchCheaperProduct(barcode: String, cityId: Int, price: Double): Response<MutableList<SupermarketProduct>>
    suspend fun confirmPurchase(token: String, purchaseList: List<ProductList>): Response<BaseResponse>
}

class ProductApiHelperImpl @Inject constructor(
    private val productService: ProductService
): ProductApiHelper {

    override suspend fun fetchProduct(barcode: String, supermarketId: Int): Response<SupermarketProduct> {
        return productService.fetchProduct(barcode, supermarketId)
    }

    override suspend fun fetchCheaperProduct(barcode: String, cityId: Int, price: Double): Response<MutableList<SupermarketProduct>> {
        return productService.fetchCheaperProduct(barcode, cityId, price)
    }

    override suspend fun confirmPurchase(token: String, purchaseList: List<ProductList>): Response<BaseResponse> {
        return productService.confirmPurchase(token, purchaseList)
    }

}