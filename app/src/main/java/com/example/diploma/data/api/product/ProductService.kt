package com.example.diploma.data.api.product

import com.example.diploma.data.model.BaseResponse
import com.example.diploma.ui.productInfo.ProductList
import com.example.diploma.ui.productInfo.SupermarketProduct
import com.example.diploma.ui.purchaseList.PurchaseListProduct
import com.example.diploma.ui.scan.PurchaseProduct
import retrofit2.Response
import retrofit2.http.*

interface ProductService {

    @GET("products/")
    suspend fun fetchProduct(
        @Query("barcode") barcode: String, @Query("supermarketId") supermarketId: Int): Response<SupermarketProduct>

    @GET("products/cheaper")
    suspend fun fetchCheaperProduct(
        @Query("barcode") barcode: String,
        @Query("cityId") cityId: Int,
        @Query("price") price: Double): Response<MutableList<SupermarketProduct>>

    @POST("purchases/confirm")
    suspend fun confirmPurchase(@Header("Authorization") token: String, @Body purchaseList: List<ProductList>): Response<BaseResponse>

}