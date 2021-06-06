package com.example.diploma.data.api.history

import com.example.diploma.ui.history.PurchaseHistory
import com.example.diploma.ui.purchase.CategoryExpenditure
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.Path
import javax.inject.Inject

interface HistoryApiHelper {
    suspend fun fetchAllHistory(token: String): Response<List<PurchaseHistory>>
    suspend fun fetchAllHistoryByDateRange(token: String, from: Long, to: Long): Response<List<PurchaseHistory>>
    suspend fun fetchHistoryById(token: String, id: Int): Response<List<CategoryExpenditure>>
}

class HistoryApiHelperImpl @Inject constructor(
    private val historyService: HistoryService
): HistoryApiHelper {

    override suspend fun fetchAllHistory(token: String): Response<List<PurchaseHistory>> {
        return historyService.fetchAllHistory(token)
    }

    override suspend fun fetchAllHistoryByDateRange(
        token: String,
        from: Long,
        to: Long
    ): Response<List<PurchaseHistory>> {
        return historyService.fetchAllHistoryByDateRange(token, from, to)
    }

    override suspend fun fetchHistoryById(
        token: String,
        id: Int
    ): Response<List<CategoryExpenditure>> {
        return historyService.fetchHistoryById(token, id)
    }

}