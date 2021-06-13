package com.example.diploma.data.repository

import com.example.diploma.data.api.product.ProductDataSource
import com.example.diploma.data.local.user.PurchaseListProductDao
import com.example.diploma.data.model.BaseResponse
import com.example.diploma.data.model.Result
import com.example.diploma.ui.productInfo.ProductList
import com.example.diploma.ui.productInfo.SupermarketProduct
import com.example.diploma.ui.purchaseList.PurchaseListProduct
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val productDataSource: ProductDataSource,
    private val purchaseListProductDao: PurchaseListProductDao
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

    suspend fun saveProductToPurchaseList(purchaseListProduct: PurchaseListProduct) {
        val product: PurchaseListProduct? = purchaseListProductDao.loadById(purchaseListProduct.id)
        if (product != null) {
            purchaseListProduct.quantity = product.quantity + 1
        }
        purchaseListProductDao.save(purchaseListProduct)
    }

    suspend fun adjustPurchaseListProductQuantity(purchaseListProduct: PurchaseListProduct) {
        purchaseListProductDao.update(purchaseListProduct)
    }

    fun loadSupermarketProducts(): Flow<List<PurchaseListProduct>> {
        return purchaseListProductDao.load().flowOn(Dispatchers.IO).conflate()
    }

    suspend fun deleteProductFromPurchaseList(purchaseListProduct: PurchaseListProduct) {
        purchaseListProductDao.delete(purchaseListProduct)
    }

    suspend fun clearPurchaseList() {
        purchaseListProductDao.deleteAll()
    }


    //suspend fun checkExistenceOfProduct(barcode: String) = purchaseListProductDao.isSupermarketProductExist(barcode)

}