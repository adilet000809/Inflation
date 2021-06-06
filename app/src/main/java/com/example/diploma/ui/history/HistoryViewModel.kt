package com.example.diploma.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diploma.data.model.Result
import com.example.diploma.data.repository.HistoryRepository
import com.example.diploma.ui.purchase.CategoryExpenditure
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val historyRepository: HistoryRepository
): ViewModel() {

    private val _historyResult = MutableLiveData<Result<List<PurchaseHistory>>>()
    val historyResult: LiveData<Result<List<PurchaseHistory>>> = _historyResult

    private val _historyByIdResult = MutableLiveData<Result<List<CategoryExpenditure>>>()
    val historyByIdResult: LiveData<Result<List<CategoryExpenditure>>> = _historyByIdResult

    fun fetchHistory(token: String) {
        viewModelScope.launch {
            historyRepository.fetchAllHistory(token).collect {
                if (it.status == Result.Status.SUCCESS) {
                    _historyResult.value = Result.success(it.data)
                }
                if (it.status == Result.Status.LOADING) {
                    _historyResult.value = Result.loading()
                }
                if (it.status == Result.Status.ERROR) {
                    _historyResult.value = Result.error(it.message ?: "Error", it.error)
                }
            }
        }
    }

    fun fetchHistoryByDateRange(token: String, from: Long, to: Long) {
        viewModelScope.launch {
            historyRepository.fetchAllHistoryByDateRange(token, from, to).collect {
                if (it.status == Result.Status.SUCCESS) {
                    _historyResult.value = Result.success(it.data)
                }
                if (it.status == Result.Status.LOADING) {
                    _historyResult.value = Result.loading()
                }
                if (it.status == Result.Status.ERROR) {
                    _historyResult.value = Result.error(it.message ?: "Error", it.error)
                }
            }
        }
    }

    fun fetchHistoryById(token: String, id: Int) {
        viewModelScope.launch {
            historyRepository.fetchHistoryById(token, id).collect {
                if (it.status == Result.Status.SUCCESS) {
                    _historyByIdResult.value = Result.success(it.data)
                }
                if (it.status == Result.Status.LOADING) {
                    _historyByIdResult.value = Result.loading()
                }
                if (it.status == Result.Status.ERROR) {
                    _historyByIdResult.value = Result.error(it.message ?: "Error", it.error)
                }
            }
        }
    }

}