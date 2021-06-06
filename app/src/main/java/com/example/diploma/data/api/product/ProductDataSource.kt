package com.example.diploma.data.api.product

import com.example.diploma.data.api.BaseDataSource
import com.example.diploma.data.model.BaseResponse
import com.example.diploma.data.model.Result
import com.example.diploma.ui.home.City
import com.example.diploma.ui.home.Supermarket
import com.example.diploma.ui.productInfo.ProductList
import com.example.diploma.ui.productInfo.SupermarketProduct
import com.example.diploma.ui.purchaseList.PurchaseListProduct
import com.example.diploma.ui.scan.PurchaseProduct
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import javax.inject.Inject

interface ProductDataSource {
    suspend fun fetchProduct(barcode: String, supermarketId: Int): Result<SupermarketProduct>
    suspend fun fetchCheaperProduct(barcode: String, cityId: Int, price: Double): Result<MutableList<SupermarketProduct>>
    suspend fun confirmPurchase(token: String, purchaseList: List<ProductList>): Result<BaseResponse>
}

class ProductDataSourceImpl @Inject constructor(
    private val productApiHelper: ProductApiHelper,
    private val retrofit: Retrofit
): ProductDataSource, BaseDataSource(retrofit) {

    override suspend fun fetchProduct(barcode: String, supermarketId: Int): Result<SupermarketProduct> {
        return getResponse(
            request = { productApiHelper.fetchProduct(barcode, supermarketId) },
            defaultErrorMessage = "Error"
        )
    }

    override suspend fun fetchCheaperProduct(barcode: String, cityId: Int, price: Double): Result<MutableList<SupermarketProduct>> {
        return getResponse(
            request = { productApiHelper.fetchCheaperProduct(barcode, cityId, price) },
            defaultErrorMessage = "Error"
        )
    }

    override suspend fun confirmPurchase(token: String, purchaseList: List<ProductList>): Result<BaseResponse> {
        return getResponse(
            request = { productApiHelper.confirmPurchase(token, purchaseList) },
            defaultErrorMessage = "Error"
        )
    }

}