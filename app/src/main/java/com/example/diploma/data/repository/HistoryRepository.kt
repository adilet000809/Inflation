package com.example.diploma.data.repository

import com.example.diploma.data.api.history.HistoryDataSource
import com.example.diploma.data.model.Result
import com.example.diploma.ui.history.PurchaseHistory
import com.example.diploma.ui.purchase.CategoryExpenditure
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject

class HistoryRepository @Inject constructor(
    private val historyDataSource: HistoryDataSource
) {
    suspend fun fetchAllHistory(token: String): Flow<Result<List<PurchaseHistory>>> {
        return flow {
            emit(Result.loading())
            val historyResult = historyDataSource.fetchAllHistory(token)
            emit(historyResult)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun fetchAllHistoryByDateRange(token: String, from: Long, to: Long): Flow<Result<List<PurchaseHistory>>> {
        return flow {
            emit(Result.loading())
            val historyResult = historyDataSource.fetchAllHistoryByDateRange(token, from, to)
            emit(historyResult)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun fetchHistoryById(token: String, id: Int): Flow<Result<List<CategoryExpenditure>>> {
        return flow {
            emit(Result.loading())
            val historyResult = historyDataSource.fetchHistoryById(token, id)
            emit(historyResult)
        }.flowOn(Dispatchers.IO)
    }
}