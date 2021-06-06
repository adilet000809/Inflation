package com.example.diploma.data.api.history

import com.example.diploma.ui.history.PurchaseHistory
import com.example.diploma.ui.purchase.CategoryExpenditure
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface HistoryService {

    @GET("history/")
    suspend fun fetchAllHistory(@Header("Authorization") token: String): Response<List<PurchaseHistory>>

    @GET("history/filter")
    suspend fun fetchAllHistoryByDateRange(
        @Header("Authorization") token: String,
        @Query("from") from: Long,
        @Query("to") to: Long): Response<List<PurchaseHistory>>

    @GET("history/{id}")
    suspend fun fetchHistoryById(
        @Header("Authorization") token: String,
        @Path("id") id: Int): Response<List<CategoryExpenditure>>

}