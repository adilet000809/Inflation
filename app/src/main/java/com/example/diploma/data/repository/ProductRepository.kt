package com.example.diploma.data.repository

import com.example.diploma.data.api.product.ProductDataSource
import com.example.diploma.data.model.BaseResponse
import com.example.diploma.data.model.Result
import com.example.diploma.ui.productInfo.ProductList
import com.example.diploma.ui.productInfo.SupermarketProduct
import com.example.diploma.ui.purchaseList.PurchaseListProduct
import com.example.diploma.ui.scan.PurchaseProduct
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val productDataSource: ProductDataSource
) {

    suspend fun fetchProduct(barcode: String, supermarketId: Int): Flow<Result<SupermarketProduct>> {
        return flow {
            emit(Result.loading())
            val productResult = productDataSource.fetchProduct(barcode, supermarketId)
            emit(productResult)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun fetchCheaperProduct(barcode: String, cityId: Int, price: Double): Flow<Result<MutableList<SupermarketProduct>>> {
        return flow {
            emit(Result.loading())
            val cheaperProductResult = productDataSource.fetchCheaperProduct(barcode, cityId, price)
            emit(cheaperProductResult)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun confirmPurchase(token: String, purchaseList: MutableList<ProductList>): Flow<Result<BaseResponse>> {
        return flow {
            emit(Result.loading())
            val purchaseResult = productDataSource.confirmPurchase(token, purchaseList)
            emit(purchaseResult)
        }.flowOn(Dispatchers.IO)
    }

}