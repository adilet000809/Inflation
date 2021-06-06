package com.example.diploma.data.api.history

import com.example.diploma.data.api.BaseDataSource
import com.example.diploma.data.model.BaseResponse
import com.example.diploma.ui.profile.UserProfile
import com.example.diploma.data.model.Result
import com.example.diploma.ui.history.PurchaseHistory
import com.example.diploma.ui.profileEdit.PasswordEditRequest
import com.example.diploma.ui.purchase.CategoryExpenditure
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Header
import retrofit2.http.Path
import javax.inject.Inject

interface HistoryDataSource {
    suspend fun fetchAllHistory(token: String): Result<List<PurchaseHistory>>
    suspend fun fetchAllHistoryByDateRange(token: String, from: Long, to: Long): Result<List<PurchaseHistory>>
    suspend fun fetchHistoryById(token: String, id: Int): Result<List<CategoryExpenditure>>
}

class HistoryDataSourceImpl @Inject constructor(
    private val historyApiHelper: HistoryApiHelper,
    retrofit: Retrofit
): HistoryDataSource, BaseDataSource(retrofit) {

    override suspend fun fetchAllHistory(token: String): Result<List<PurchaseHistory>> {
        return getResponse(
            request = { historyApiHelper.fetchAllHistory(token) },
            defaultErrorMessage = "Error"
        )
    }

    override suspend fun fetchAllHistoryByDateRange(
        token: String,
        from: Long,
        to: Long
    ): Result<List<PurchaseHistory>> {
        return getResponse(
            request = { historyApiHelper.fetchAllHistoryByDateRange(token, from, to) },
            defaultErrorMessage = "Error"
        )
    }

    override suspend fun fetchHistoryById(
        token: String,
        id: Int
    ): Result<List<CategoryExpenditure>> {
        return getResponse(
            request = { historyApiHelper.fetchHistoryById(token, id) },
            defaultErrorMessage = "Error"
        )
    }

}