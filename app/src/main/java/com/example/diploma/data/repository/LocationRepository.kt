package com.example.diploma.data.repository

import com.example.diploma.data.api.location.LocationDataSource
import com.example.diploma.data.model.Result
import com.example.diploma.ui.home.City
import com.example.diploma.ui.home.Supermarket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class LocationRepository @Inject constructor(
    private val locationDataSource: LocationDataSource
) {

    suspend fun fetchCity(): Flow<Result<List<City>>> {
        return flow {
            emit(Result.loading())
            val cityResult = locationDataSource.fetchCity()
            emit(cityResult)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun fetchSupermarket(cityId: Int): Flow<Result<List<Supermarket>>> {
        return flow {
            emit(Result.loading())
            val supermarketResult = locationDataSource.fetchSupermarket(cityId)
            emit(supermarketResult)
        }.flowOn(Dispatchers.IO)
    }

}